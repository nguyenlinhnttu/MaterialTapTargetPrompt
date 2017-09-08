/*
 * Copyright (C) 2016 Samuel Wall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.samuelwall.materialtaptargetprompt;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = uk.co.samuelwall.materialtaptargetprompt.BuildConfig.class, sdk = 22)
public class MaterialTapTargetPromptUnitTest
{
    private static int SCREEN_WIDTH = 1080;
    private static int SCREEN_HEIGHT = 1920;

    private int stateProgress;

    @Before
    public void setup()
    {
        
    }

    @After
    public void after()
    {
        if (stateProgress > 0)
        {
            Assert.assertEquals(4, stateProgress);
        }
        stateProgress = -1;
    }

    @Test
    public void promptFromVariables()
    {
        LinearInterpolator interpolator = new LinearInterpolator();
        MaterialTapTargetPrompt.Builder builder = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
            .setTarget(50, 40)
            .setPrimaryText("Primary text")
            .setSecondaryText("Secondary text")
            .setMaxTextWidth(600f)
            .setTextPadding(50f)
            .setBackgroundColour(Color.BLUE)
            .setFocalColour(Color.GREEN)
            .setFocalRadius(55f)
            .setTextSeparation(22f)
            .setPrimaryTextSize(30f)
            .setSecondaryTextSize(20f)
            .setPrimaryTextColour(Color.CYAN)
            .setSecondaryTextColour(Color.GRAY)
            .setFocalPadding(30f)
            .setAnimationInterpolator(interpolator);

        assertTrue(builder.isTargetSet());
        MaterialTapTargetPrompt prompt = builder.show();

        assertEquals(600f, prompt.mMaxTextWidth, 0.0f);
        assertEquals(50f, prompt.mTextPadding, 0.0f);
        assertNull(prompt.mTargetView);
        assertEquals(50f, prompt.mTargetPosition.x, 0.0f);
        assertEquals(40f, prompt.mTargetPosition.y, 0.0f);
        assertEquals(30f, prompt.mPaintPrimaryText.getTextSize(), 0f);
        assertEquals(20f, prompt.mPaintSecondaryText.getTextSize(), 0f);
        assertEquals(Color.CYAN, prompt.mPaintPrimaryText.getColor());
        assertEquals(Color.GRAY, prompt.mPaintSecondaryText.getColor());
        assertEquals(interpolator, prompt.mAnimationInterpolator);
        assertEquals(30f, prompt.mView.mPromptFocal.getPadding(), 0.0f);

        assertEquals("Primary text", prompt.mView.mPrimaryTextLayout.getText().toString());
        assertEquals("Secondary text", prompt.mView.mSecondaryTextLayout.getText().toString());
        assertEquals(22f, prompt.mView.mTextSeparation, 0.0f);

        assertTrue(prompt.mView.mPromptBackground instanceof CirclePromptBackground);
        CirclePromptBackground promptBackground = (CirclePromptBackground) prompt.mView.mPromptBackground;
        assertEquals(Color.BLUE, promptBackground.mPaint.getColor());

        assertTrue(prompt.mView.mPromptFocal instanceof CirclePromptFocal);
        CirclePromptFocal promptFocal = (CirclePromptFocal) prompt.mView.mPromptFocal;
        assertEquals(Color.GREEN, promptFocal.mPaint.getColor());
        assertEquals(55f, promptFocal.mBaseRadius, 0.0f);

        prompt.dismiss();
        prompt.mAnimationCurrent.end();
        assertNull(prompt.mView.getParent());
    }


    @Test
    public void promptFromVariablesWithCharSequences()
    {
        LinearInterpolator interpolator = new LinearInterpolator();
        MaterialTapTargetPrompt.Builder builder = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(50, 40)
                .setPrimaryText(new SpannableStringBuilder("Primary text"))
                .setSecondaryText(new SpannableStringBuilder("Secondary text"))
                .setMaxTextWidth(600f)
                .setTextPadding(50f)
                .setBackgroundColour(Color.BLUE)
                .setFocalColour(Color.GREEN)
                .setFocalRadius(55f)
                .setTextSeparation(22f)
                .setPrimaryTextSize(30f)
                .setSecondaryTextSize(20f)
                .setPrimaryTextColour(Color.CYAN)
                .setSecondaryTextColour(Color.GRAY)
                .setFocalPadding(30f)
                .setAnimationInterpolator(interpolator);

        assertTrue(builder.isTargetSet());
        MaterialTapTargetPrompt prompt = builder.show();

        assertEquals(600f, prompt.mMaxTextWidth, 0.0f);
        assertEquals(50f, prompt.mTextPadding, 0.0f);
        assertNull(prompt.mTargetView);
        assertEquals(50f, prompt.mTargetPosition.x, 0.0f);
        assertEquals(40f, prompt.mTargetPosition.y, 0.0f);
        assertEquals(30f, prompt.mPaintPrimaryText.getTextSize(), 0f);
        assertEquals(20f, prompt.mPaintSecondaryText.getTextSize(), 0f);
        assertEquals(Color.CYAN, prompt.mPaintPrimaryText.getColor());
        assertEquals(Color.GRAY, prompt.mPaintSecondaryText.getColor());
        assertEquals(interpolator, prompt.mAnimationInterpolator);
        assertEquals(30f, prompt.getPromptFocal().getPadding(), 0.0f);

        assertEquals("Primary text", prompt.mView.mPrimaryTextLayout.getText().toString());
        assertEquals("Secondary text", prompt.mView.mSecondaryTextLayout.getText().toString());
        assertEquals(22f, prompt.mView.mTextSeparation, 0.0f);

        assertTrue(prompt.mView.mPromptBackground instanceof CirclePromptBackground);
        CirclePromptBackground promptBackground = (CirclePromptBackground) prompt.mView.mPromptBackground;
        assertEquals(Color.BLUE, promptBackground.mPaint.getColor());

        assertTrue(prompt.mView.mPromptFocal instanceof CirclePromptFocal);
        CirclePromptFocal promptFocal = (CirclePromptFocal) prompt.mView.mPromptFocal;
        assertEquals(Color.GREEN, promptFocal.mPaint.getColor());
        assertEquals(55f, promptFocal.mBaseRadius, 0.0f);

        prompt.dismiss();
        prompt.mAnimationCurrent.end();
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptNotCreatedWhenTargetNotSet()
    {
        MaterialTapTargetPrompt.Builder builder = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setPrimaryText("Primary text")
                .setSecondaryText("Secondary text");
        assertNull(builder.create());
    }

    @Test
    public void promptNotCreatedWhenTextNotSet()
    {
        MaterialTapTargetPrompt.Builder builder = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(50, 40);
        assertNull(builder.create());
    }

    @Test
    public void promptCreatedWhenPrimaryTextNotSet()
    {
        MaterialTapTargetPrompt.Builder builder = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(50, 40)
                .setSecondaryText("Secondary text");
        MaterialTapTargetPrompt prompt = builder.create();
        assertNotNull(prompt);
        prompt.show();

        assertNull(prompt.mView.mPrimaryTextLayout);

        prompt.finish();
        prompt.mAnimationCurrent.end();
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptCreatedWhenSecondaryTextNotSet()
    {
        MaterialTapTargetPrompt.Builder builder = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(50, 40)
                .setPrimaryText("Primary text");
        MaterialTapTargetPrompt prompt = builder.create();
        assertNotNull(prompt);
        prompt.show();

        assertNull(prompt.mView.mSecondaryTextLayout);

        prompt.finish();
        prompt.mAnimationCurrent.end();
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptAnimationCancel()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .show();
        prompt.mAnimationCurrent.cancel();
        assertEquals(1f, prompt.mRevealedAmount, 0f);

        prompt.dismiss();
        assertNotNull(prompt.mAnimationCurrent);
        prompt.mAnimationCurrent.cancel();
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptCancelFinishAnimation()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(final MaterialTapTargetPrompt prompt, final int state)
                    {

                    }
                })
                .show();

        prompt.finish();
        assertNotNull(prompt.mAnimationCurrent);
        prompt.mAnimationCurrent.cancel();
        assertNull(prompt.mAnimationCurrent);
        assertNull(prompt.mView.getParent());
    }

    @Test
    public void promptTouchEventFocal()
    {
        stateProgress = 0;
        final MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, state);
                            if (prompt.mAnimationCurrent != null)
                            {
                                prompt.mAnimationCurrent.end();
                            }
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, state);
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, state);
                        }
                        else
                        {
                            fail();
                        }
                        stateProgress++;
                    }
                })
                .show();
        assertFalse(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalCaptureEvent()
    {
        stateProgress = 0;
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setCaptureTouchEventOnFocal(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FOCAL_PRESSED, state);
                            if (prompt.mAnimationCurrent != null)
                            {
                                prompt.mAnimationCurrent.end();
                            }
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHING, state);
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_FINISHED, state);
                        }
                        else
                        {
                            fail();
                        }
                        stateProgress++;
                    }
                })
                .show();
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventFocalNoListener()
    {
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setCaptureTouchEventOnFocal(true)
                .show();
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 10, 10, 0)));
    }

    @Test
    public void promptTouchEventBackground()
    {
        stateProgress = 0;
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, state);
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            if (prompt.mAnimationCurrent != null)
                            {
                                prompt.mAnimationCurrent.end();
                            }
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                        }
                        else
                        {
                            fail();
                        }
                        stateProgress++;
                    }
                })
                .show();
        assertTrue(prompt.mView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 60, 60, 0)));
    }

    @Test
    public void testPromptBackButtonDismiss()
    {
        stateProgress = 0;
        MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340)
                .setTarget(10, 10)
                .setPrimaryText("Primary text")
                .setBackButtonDismissEnabled(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(final MaterialTapTargetPrompt prompt, final int state)
                    {
                        if (stateProgress == 0)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALING, state);
                        }
                        else if (stateProgress == 1)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_REVEALED, state);
                        }
                        else if (stateProgress == 2)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED, state);
                        }
                        else if (stateProgress == 3)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSING, state);
                            if (prompt.mAnimationCurrent != null)
                            {
                                prompt.mAnimationCurrent.end();
                            }
                        }
                        else if (stateProgress == 4)
                        {
                            assertEquals(MaterialTapTargetPrompt.STATE_DISMISSED, state);
                        }
                        else
                        {
                            fail();
                        }
                        stateProgress++;
                    }
                })
                .show();
        final KeyEvent.DispatcherState dispatchState = new KeyEvent.DispatcherState();
        Mockito.doAnswer(new Answer<KeyEvent.DispatcherState>()
        {
            @Override
            public KeyEvent.DispatcherState answer(final InvocationOnMock invocation)
                    throws Throwable
            {
                return dispatchState;
            }
        })
        .when(prompt.mView).getKeyDispatcherState();
        assertTrue(prompt.mView.dispatchKeyEventPreIme(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)));
        assertTrue(prompt.mView.dispatchKeyEventPreIme(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK)));
    }

    @Test
    public void promptCentreLeft()
    {
        final MaterialTapTargetPrompt prompt = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 300)
                .setPrimaryText("Primary text")
                .setTarget(90, 90)
                .show();
        if (prompt.mAnimationCurrent != null)
        {
            prompt.mAnimationCurrent.end();
        }

        assertEquals(90, prompt.mTargetPosition.x, 0);
        assertEquals(90, prompt.mTargetPosition.y, 0);
        assertTrue(prompt.mInside88dpBounds);
        assertFalse(prompt.mHorizontalTextPositionLeft);


        assertTrue(prompt.mView.mPromptBackground instanceof CirclePromptBackground);
        CirclePromptBackground promptBackground = (CirclePromptBackground) prompt.mView.mPromptBackground;
        assertEquals(190, promptBackground.mBaseRadius, 1);
        assertEquals(190, promptBackground.mBasePosition.x, 1);
        assertEquals(147, promptBackground.mBasePosition.y, 1);

        assertTrue(prompt.mView.mPromptFocal instanceof CirclePromptFocal);
        CirclePromptFocal promptFocal = (CirclePromptFocal) prompt.mView.mPromptFocal;
        assertEquals(44, promptFocal.mBaseRadius, 0);
    }

    @SuppressLint("RtlHardcoded")
    @Test
    public void testGetTextAlignment()
    {
        final MaterialTapTargetPrompt.Builder builder = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340);
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.START, "abc"));
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.LEFT, "abc"));

        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.END, "abc"));
        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.RIGHT, "abc"));

        assertEquals(Layout.Alignment.ALIGN_CENTER, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.CENTER_HORIZONTAL, "abc"));

        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.START, "جبا"));
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.LEFT, "جبا"));

        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.END, "جبا"));
        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.RIGHT, "جبا"));

        assertEquals(Layout.Alignment.ALIGN_CENTER, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.CENTER_HORIZONTAL, "جبا"));

        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", 16);

        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.START, "abc"));
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.LEFT, "abc"));

        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.END, "abc"));
        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.RIGHT, "abc"));

        assertEquals(Layout.Alignment.ALIGN_CENTER, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.CENTER_HORIZONTAL, "abc"));

        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.START, "جبا"));
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.LEFT, "جبا"));

        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.END, "جبا"));
        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.RIGHT, "جبا"));

        assertEquals(Layout.Alignment.ALIGN_CENTER, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.CENTER_HORIZONTAL, "جبا"));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("RtlHardcoded")
    @Test
    public void testGetTextAlignmentRtl()
    {
        final MaterialTapTargetPrompt.Builder builder = createBuilder(SCREEN_WIDTH, SCREEN_HEIGHT, 340);
        Mockito.doAnswer(new Answer<Resources>()
        {
            @Override
            public Resources answer(InvocationOnMock invocation) throws Throwable
            {
                final Resources resources = spy((Resources) invocation.callRealMethod());
                Mockito.doAnswer(new Answer<Configuration>()
                {
                    @Override
                    public Configuration answer(InvocationOnMock invocation) throws Throwable
                    {
                        final Configuration configuration = spy((Configuration) invocation.callRealMethod());
                        when(configuration.getLayoutDirection()).thenReturn(View.LAYOUT_DIRECTION_RTL);
                        return configuration;
                    }
                }).when(resources).getConfiguration();
                return resources;
            }
        }).when(builder.mResourceFinder).getResources();

        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.START, "abc"));
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.LEFT, "abc"));

        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.END, "abc"));
        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.RIGHT, "abc"));

        assertEquals(Layout.Alignment.ALIGN_CENTER, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.CENTER_HORIZONTAL, "abc"));

        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.START, "جبا"));
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.LEFT, "جبا"));

        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.END, "جبا"));
        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.RIGHT, "جبا"));

        assertEquals(Layout.Alignment.ALIGN_CENTER, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.CENTER_HORIZONTAL, "جبا"));

        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", 16);

        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.START, "abc"));
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.LEFT, "abc"));

        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.END, "abc"));
        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.RIGHT, "abc"));

        assertEquals(Layout.Alignment.ALIGN_CENTER, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.CENTER_HORIZONTAL, "abc"));

        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.START, "جبا"));
        assertEquals(Layout.Alignment.ALIGN_NORMAL, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.LEFT, "جبا"));

        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.END, "جبا"));
        assertEquals(Layout.Alignment.ALIGN_OPPOSITE, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.RIGHT, "جبا"));

        assertEquals(Layout.Alignment.ALIGN_CENTER, PromptUtils.getTextAlignment(builder.mResourceFinder, Gravity.CENTER_HORIZONTAL, "جبا"));
    }

    @Test
    public void testParseTintMode()
    {
        assertEquals(PromptUtils.parseTintMode(-1, null), null);
        assertEquals(PromptUtils.parseTintMode(3, null), PorterDuff.Mode.SRC_OVER);
        assertEquals(PromptUtils.parseTintMode(5, null), PorterDuff.Mode.SRC_IN);
        assertEquals(PromptUtils.parseTintMode(9, null), PorterDuff.Mode.SRC_ATOP);
        assertEquals(PromptUtils.parseTintMode(14, null), PorterDuff.Mode.MULTIPLY);
        assertEquals(PromptUtils.parseTintMode(15, null), PorterDuff.Mode.SCREEN);
        assertEquals(PromptUtils.parseTintMode(16, null), PorterDuff.Mode.ADD);
    }

    private MaterialTapTargetPrompt.Builder createBuilder(final int screenWidth,
                                              final int screenHeight, final float primaryTextWidth)
    {
        final Activity activity = spy(Robolectric.buildActivity(Activity.class).create().get());
        final FrameLayout layout = spy(new FrameLayout(activity));
        final ResourceFinder resourceFinder = spy(new ActivityResourceFinder(activity));
        activity.setContentView(layout);
        setViewBounds(layout, screenWidth, screenHeight);
        final MaterialTapTargetPrompt.Builder builder = spy(new MaterialTapTargetPrompt.Builder(resourceFinder, 0));
        Mockito.doAnswer(new Answer<MaterialTapTargetPrompt>()
            {
                @Override
                public MaterialTapTargetPrompt answer(final InvocationOnMock invocation)
                        throws Throwable
                {
                    final MaterialTapTargetPrompt basePrompt = (MaterialTapTargetPrompt) invocation.callRealMethod();
                    if (basePrompt != null)
                    {
                        final MaterialTapTargetPrompt prompt = spy(basePrompt);
                        prompt.mView = spy(prompt.mView);
                        when(prompt.calculateMaxTextWidth(prompt.mView.mPrimaryTextLayout))
                                .thenReturn(primaryTextWidth);

                        Mockito.doAnswer(new Answer<Void>()
                        {
                            public Void answer(InvocationOnMock invocation)
                            {
                                try
                                {
                                    invocation.callRealMethod();
                                }
                                catch (final Throwable throwable)
                                {
                                    throwable.printStackTrace();
                                }
                                prompt.mView.mClipToBounds = true;
                                prompt.mView.mClipBounds.set(0, 0, screenWidth, screenHeight);
                                prompt.mClipViewBoundsInset88dp.left = prompt.mView.mClipBounds.left + prompt.m88dp;
                                prompt.mClipViewBoundsInset88dp.right = prompt.mView.mClipBounds.right - prompt.m88dp;
                                prompt.mClipViewBoundsInset88dp.top = prompt.mView.mClipBounds.top + prompt.m88dp;
                                prompt.mClipViewBoundsInset88dp.bottom = prompt.mView.mClipBounds.bottom - prompt.m88dp;
                                return null;
                            }
                        }).when(prompt).updateClipBounds();

                        Mockito.doAnswer(new Answer<Void>()
                        {
                            public Void answer(InvocationOnMock invocation)
                            {
                                try
                                {
                                    invocation.callRealMethod();
                                }
                                catch (Throwable throwable)
                                {
                                    throwable.printStackTrace();
                                }
                                prompt.updateFocalCentrePosition();
                                prompt.mView.mPromptFocal.update(prompt, 1, 1);
                                prompt.mView.mPromptFocal.updateRipple(1, 1);
                                prompt.mView.mPromptBackground.update(prompt, 1, 1);
                                if (prompt.mPaintSecondaryText != null)
                                {
                                    prompt.mPaintSecondaryText.setAlpha(prompt.mSecondaryTextColourAlpha);
                                }
                                if (prompt.mPaintPrimaryText != null)
                                {
                                    prompt.mPaintPrimaryText.setAlpha(prompt.mPrimaryTextColourAlpha);
                                }
                                return null;
                            }
                        }).when(prompt).show();
                        return prompt;
                    }
                    return null;
                }
            }).when(builder).create();
        return builder;
    }

    private void setViewBounds(final View view, final int width, final int height)
    {
        //TODO make this work for all versions
        view.setLeft(0);
        view.setRight(0);
        view.setRight(width);
        view.setBottom(height);
        final ViewParent parent = view.getParent();
        if (parent != null && ((View) parent).getRight() != 0 && ((View) parent).getBottom() != 0)
        {
            setViewBounds(((View) parent), width, height);
        }
    }
}
