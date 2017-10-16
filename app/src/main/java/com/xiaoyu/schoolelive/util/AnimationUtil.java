package com.xiaoyu.schoolelive.util;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;


import com.xiaoyu.schoolelive.custom.CustomFloatingDraftButton;

import java.util.ArrayList;
import java.util.Random;


public class AnimationUtil{
    public static void slideview(final View view, final float p1, final float p2, final float p3, final float p4,
                                 long durationMillis, long delayMillis,
                                 final boolean startVisible,final boolean endVisible) {
        //如果处在动画阶段则不允许再次运行动画
        if(view.getTag()!= null && "-1".equals(view.getTag().toString())){
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(p1, p2, p3, p4);
        animation.setDuration(durationMillis);
        animation.setStartOffset(delayMillis);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(startVisible){
                    view.setVisibility(View.VISIBLE);
                }else{
                    view.setVisibility(View.INVISIBLE);
                }
                view.setTag("-1");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                int left = view.getLeft()+(int)(p2-p1);
                int top = view.getTop()+(int)(p4-p3);
                int width = view.getWidth();
                int height = view.getHeight();
                view.layout(left, top, left+width, top+height);//重新设置位置
                if(endVisible){
                    view.setVisibility(View.VISIBLE);
                }else{
                    view.setVisibility(View.INVISIBLE);
                }
                view.setTag("1");

            }
        });
        if(endVisible){
            view.startAnimation(animation);
        }else{//如果关闭则加渐变效果
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setDuration(durationMillis);
            animationSet.setStartOffset(delayMillis);
            animationSet.addAnimation(animation);

            AlphaAnimation alphaAnimation= new AlphaAnimation(1.0f, 0.0f);
            animationSet.addAnimation(alphaAnimation);

            view.startAnimation(animationSet);
        }

    }

    //移动Buttons展开或者关闭
    public static void slideButtons(Context context,final CustomFloatingDraftButton button){

        int size = button.getButtonSize();
        if(size == 0){
            return;
        }
        //主按钮的左侧坐标(离左侧的距离)
        int buttonLeft = button.getLeft();
        //屏幕宽度
        int screenWidth = ScreenUtil.getScreenWidth(context);
        //主按钮离右侧的距离
        int buttonRight = screenWidth - button.getRight();
        //主按钮的上侧坐标(离上侧的距离)
        int buttonTop = button.getTop() - 150;
        //主按钮离下侧的距离
        int buttonBottom = ScreenUtil.getScreenHeight(context) - button.getBottom() - 200;
        //主按钮的宽度
        int buttonWidth = button.getWidth();
        //展开按钮与主按钮的间隔(中心至中心的距离)
        int radius = 5*buttonWidth/4;
        //展开按钮之间的间隔(中心至中心的距离)
        int gap = 5*buttonWidth/4;

        ArrayList<FloatingActionButton> buttons = button.getButtons();

        if(button.isDraftable()){//可拖拽 展开Buttons
            showRotateAnimation(button,0,225);
            //可围成圆形
            //离上下左右的距离都要大于展开按钮与主按钮之间的间隔距离
            if(buttonLeft >= radius && buttonRight >= radius
                    && buttonTop>= radius && buttonBottom>= radius){
                double angle = 360.0/size;
                int randomDegree = new Random().nextInt(180);
                for(int i=0;i<size;i++){
                    FloatingActionButton faButton = buttons.get(i);
                    slideview(faButton,0, radius*(float)Math.cos(Math.toRadians(randomDegree+angle*i)),0,radius*(float)Math.sin(Math.toRadians(randomDegree+angle*i)),500,0,true,true);
                }
            }else if(size*gap < screenWidth &&(buttonTop>3*buttonBottom || buttonBottom>3*buttonTop)){
                //size*gap 表示 展开按钮所占的总长度。。。太靠近上侧或者太靠近下侧
                int leftNumber = buttonLeft/gap;//左侧能放置按钮的数量
                int rightNumber = buttonRight/gap;//右侧能放置按钮的数量
                if(buttonTop >= radius && buttonBottom >= radius){
                    //离上侧的距离与离下侧的距离均大于主按钮与展开按钮的的间隔
                    if(buttonTop >buttonBottom){//如果靠下侧，就将展开按钮显示在主按钮上方
                        FloatingActionButton faButton = buttons.get(0);
                        slideview(faButton,0,0,0,-radius,500,0,true,true);
                        for(int i=1;i<=leftNumber && i<size;i++){
                            faButton = buttons.get(i);
                            slideview(faButton,0, -gap*i,0, -radius,500,0,true,true);
                        }
                        for(int i=1;i<=rightNumber && i<size-leftNumber;i++){
                            faButton = buttons.get(i+leftNumber);
                            slideview(faButton,0, gap*i,0, -radius,500,0,true,true);
                        }
                    }else if(buttonBottom > buttonTop){//如果靠近上侧，则显示在下方
                        FloatingActionButton faButton = buttons.get(0);
                        slideview(faButton,0,0,0,radius,500,0,true,true);
                        for(int i=1;i<=leftNumber && i<size;i++){
                            faButton = buttons.get(i);
                            slideview(faButton,0, -gap*i,0, radius,500,0,true,true);
                        }
                        for(int i=1;i<=rightNumber && i<size-leftNumber;i++){
                            faButton = buttons.get(i+leftNumber);
                            slideview(faButton,0, gap*i,0, radius,500,0,true,true);
                        }
                    }
                } else if(buttonTop >= radius){//与下侧小于radius
                    FloatingActionButton faButton = buttons.get(0);
                    slideview(faButton,0,0,0,-radius,500,0,true,true);
                    for(int i=1;i<=leftNumber && i<size;i++){
                        faButton = buttons.get(i);
                        slideview(faButton,0, -gap*i,0, -radius,500,0,true,true);
                    }
                    for(int i=1;i<=rightNumber && i<size-leftNumber;i++){
                        faButton = buttons.get(i+leftNumber);
                        slideview(faButton,0, gap*i,0, -radius,500,0,true,true);
                    }
                }else if(buttonBottom >= radius){//与上侧距离小于radius
                    FloatingActionButton faButton = buttons.get(0);
                    slideview(faButton,0,0,0,radius,500,0,true,true);
                    for(int i=1;i<=leftNumber && i<size;i++){
                        faButton = buttons.get(i);
                        slideview(faButton,0, -gap*i,0, radius,500,0,true,true);
                    }
                    for(int i=1;i<=rightNumber && i<size-leftNumber;i++){
                        faButton = buttons.get(i+leftNumber);
                        slideview(faButton,0, gap*i,0, radius,500,0,true,true);
                    }
                }
            }else {
                //靠左或者靠右
                int upNumber = buttonTop/gap;//上侧能放置按钮的数量
                int belowNumber = buttonBottom/gap;//下侧能放置按钮的数量
                if((upNumber+belowNumber+1)>=size){//0号位的按钮始终位于主按钮的正方向，所以要加1
                    upNumber = upNumber*(size-1)/(upNumber+belowNumber);
                    belowNumber = size-1-upNumber;
                    if(buttonLeft >= radius){//靠右
                        FloatingActionButton faButton = buttons.get(0);
                        slideview(faButton,0, -radius,0,0,500,0,true,true);
                        for(int i=1;i<=upNumber && i<size;i++){
                            faButton = buttons.get(i);
                            slideview(faButton,0, -radius,0, -gap*i,500,0,true,true);
                        }
                        for(int i=1;i<=belowNumber && i<size-upNumber;i++){
                            faButton = buttons.get(i+upNumber);
                            slideview(faButton,0, -radius,0, gap*i,500,0,true,true);
                        }
                    }else if(buttonRight >= radius ){//靠左
                        FloatingActionButton faButton = buttons.get(0);
                        slideview(faButton,0, radius,0,0,500,0,true,true);
                        for(int i=1;i<=upNumber && i<size;i++){
                            faButton = buttons.get(i);
                            slideview(faButton,0, radius,0, -gap*i,500,0,true,true);
                        }
                        for(int i=1;i<=belowNumber && i<size-upNumber;i++){
                            faButton = buttons.get(i+upNumber);
                            slideview(faButton,0, radius,0, gap*i,500,0,true,true);
                        }
                    }
                }
            }
        }else{ //关闭Buttons
            showRotateAnimation(button,225,0);
            for(FloatingActionButton faButton : buttons){
                int faButtonLeft = faButton.getLeft();
                int faButtonTop = faButton.getTop();
                slideview(faButton,0,buttonLeft-faButtonLeft,0,buttonTop-faButtonTop+150,500,0,true,false);
            }
        }
    }

    /**旋转的动画
     * @param mView            需要选择的View
     * @param startDegress    初始的角度【从这个角度开始】
     * @param degrees        当前需要旋转的角度【转到这个角度来】
     */
    public static void showRotateAnimation(View mView,int startDegress,int degrees)
    {
        mView.clearAnimation();
        float centerX = mView.getWidth() / 2.0f;
        float centerY = mView.getHeight() / 2.0f;
        //这个是设置需要旋转的角度（也是初始化），我设置的是当前需要旋转的角度
        RotateAnimation rotateAnimation = new RotateAnimation(startDegress,degrees,centerX,centerY);//centerX和centerY是旋转View时候的锚点
        //这个是设置动画时间的
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new AccelerateInterpolator());
        //动画执行完毕后是否停在结束时的角度上
        rotateAnimation.setFillAfter(true);
        //启动动画
        mView.startAnimation(rotateAnimation);
    }
}