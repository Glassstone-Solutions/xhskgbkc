package net.glassstones.thediarymagazine.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Thompson on 10/07/2016.
 * For The Diary Magazine
 */
public class DateSerializer implements JsonSerializer<Date> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);

    public JsonElement serialize (Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.format(date));
    }
}
