import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.hyp.plugin.workday.WorkDayFactory;
import com.hyp.plugin.workday.PropertiesHelper;
import junit.framework.TestCase;

/**
 * Created by hyp on 2017/1/18.
 */
public class WorkDayTest extends TestCase {
    public void testisWorkDay(){
        String workDay = ""+WorkDayFactory.isWorkDay(new String[]{"20160303"});
        assertEquals("true",workDay);
        workDay = ""+ WorkDayFactory.isWorkDay(new String[]{"20170303"});
        assertEquals("true",workDay);
//        PropertiesHelper.setUrl("aaa");
//        workDay = WorkDayFactory.isWorkDay(new String[]{"20170303"});
//        assertEquals("Check your connection Or updateWorkDays",workDay);
//        PropertiesHelper.setUrl(null);
        workDay =""+ WorkDayFactory.isWorkDay(new String[]{});
        assertEquals("check your parameter !",workDay);

    }


    public void testUpdateWorkDay(){
        String s = WorkDayFactory.updateWorkDay(new String[]{"2017"});
        assertEquals("Success",s);
        String workDay = ""+ WorkDayFactory.isWorkDay(new String[]{"20170303"});
        assertEquals("true",workDay);
    }

    public void testWorkDay(){
        String workDay = WorkDayFactory.getIsWorkDay(new String[]{"20160303", "20170303"});
        try {
            JSONObject expected = new JSONObject("{\"20170303\":\"Plugin-WorkDay_holiday\",\"20160303\":\"Plugin-WorkDay_holiday\"}");
            JSONObject actual = new JSONObject(workDay);
            assertEquals(expected.optString("20160303"),actual.optString("20160303"));
            assertEquals(expected.optString("20170303"),actual.optString("20170303"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if("{\"20170303\":\"0\"}".equals(WorkDayFactory.httpGet("20170303"))){
            assertEquals("","");
        }else{
            assertEquals("s","");
        }
    }

    public void testCircleDay(){
        String s = ""+ WorkDayFactory.circleWorkDay(new String[]{"20170118", "2"});//today 20170119
        assertEquals("false",s);

        String actual =""+ WorkDayFactory.circleWorkDay(new String[]{"20170112", "2"});
        assertEquals("false", actual);
        actual = ""+WorkDayFactory.circleWorkDay(new String[]{"20170112", "5"});
        assertEquals("true", actual);
    }



}
