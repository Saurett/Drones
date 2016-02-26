package texium.mx.drones.utils;

/**
 * Created by texiumuser on 25/02/2016.
 */
public final class Constants {

    //DATA STATUS
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;

    //TASKS ID
    public static final int NEWS_TASK = 3;
    public static final int PROGRESS_TASK = 4;
    public static final int PENDING_TASK = 5;
    public static final int CLOSE_TASK = 6;
    public static final int REVISION_TASK = 7;

    //MAP ACCESS
    public static final String JSON_DATA_ACCESS_TASK_TYPE = "task_type";

    //TOKEN ACCESS
    public static final Long TOKEN_KEY_ACCESS_TASK_VIEW = 1L;
    public static final Long TOKEN_KEY_ACCESS_TASK_ADAPTER = 2L;
    public static final Long TOKEN_KEY_ACCESS_TASK_CLASS = 3L;
    public static final Long TOKEN_KEY_ACCESS_TASK_CLASS_DECODE = 4L;

    //Fragments tags
    public static final String FRAGMENT_NEWS_TAG = "fragment_news_task";
    public static final String FRAGMENT_PROGRESS_TAG = "fragment_progress_task";
    public static final String FRAGMENT_PENDING_TAG = "fragment_pending_task";
    public static final String FRAGMENT_REVISION_TAG = "fragment_revision_task";
    public static final String FRAGMENT_CLOSE_TAG = "fragment_close_task";
    public static final String FRAGMENT_FINISH_TAG = "fragment_finish_task";


}
