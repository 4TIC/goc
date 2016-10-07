package es.uji.apps.goc.services.rest;

import es.uji.apps.goc.dto.ResponseMessage;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ResponseHasDataMatcher extends TypeSafeMatcher<ResponseMessage>
{
    @Override
    public boolean matchesSafely(ResponseMessage responseMessage)
    {
        return responseMessage.getData().size() > 0;
    }

    public void describeTo(Description description)
    {
        description.appendText("no data returned");
    }

    @Factory
    public static <T> Matcher<ResponseMessage> hasData()
    {
        return new ResponseHasDataMatcher();
    }
}