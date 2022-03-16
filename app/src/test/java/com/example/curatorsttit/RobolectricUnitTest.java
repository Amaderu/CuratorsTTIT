package com.example.curatorsttit;

import android.content.Context;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.example.curatorsttit.common.Converter;

@Config(sdk = 24, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class RobolectricUnitTest {
    @Mock(extraInterfaces = Context.class)
    Context context;

    @Test
    public void ConvertHtmlFormatToTXT()  {
        //Context context = mock(Context.class);
        SplashActivity activity = new SplashActivity();
        assertTrue("Network os available", activity.isNetworkAvailable(context));
        assertFalse("Network os unavailable", activity.isNetworkAvailable(context));
    }

    @Test
    public void parseMD5(){
        Converter converter = new Converter();
        Uri correct = Uri.parse("http://93.174.95.29/main/2448000/A3DCB4D229DE6FDE0DB5686DEE47145D/test_book.pdf");
        String md5case1= "\"A3DCB4D229DE6FDE0DB5686DEE47145D\"";
        String md5case2 = "A3DCB4D229DE6FDE0DB5686DEE47145D";
        String md5empty = "";
        assertEquals(converter.MD5(md5case1), correct);
        assertEquals(converter.MD5(md5case2), correct);
        assertEquals(converter.MD5(md5case2), correct);
    }


}