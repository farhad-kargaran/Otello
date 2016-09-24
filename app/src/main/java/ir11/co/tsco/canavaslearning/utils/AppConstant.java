package ir11.co.tsco.canavaslearning.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppConstant
{

    public enum NetType
    {
        WIFI, MOBILE_DATA, ROAMING,NOT_ACCESS
    }


    public final static int ConnectionPingTimeout = 5000;//5 s

    public final static String IS_TYPING = "typing";
    public final static String IM_DATATYPE_TEXT = "msgText";
    public final static String IM_DATATYPE_STICKER = "msgSticker";
    public final static String IM_DATATYPE_ANIMATED_STICKER = "msgAnimatedSticker";
    public final static String IM_DATATYPE_IMAGE = "msgImage";
    public final static String IM_DATATYPE_VIDEO = "msgVideo";
    public final static String IM_DATATYPE_MIC = "msgMic";
    public final static String IM_DATATYPE_GIF = "msgGif";


    public static final int TYPE_TITLE   = -1;
    public static final int TYPE_SEPARATOR   = 0;
    public static final int TYPE_ITEM        = 1;
    public static final int TYPE_ITEM_TOGGLE = 2;
    public static final int TYPE_ITEM_MULTI  = 3;
    public static final int TYPE_ITEM_DETAIL  = -2;

    //public final static String IM_DATATYPE_UNREAD = "msgUnread";

    //public final static Set< Long > CANCEL_LIST_IMAGE = new HashSet<>();

    public static final Set< Long > canceledUploadIds = new HashSet<>();
    public static volatile  Map< Long, Long > downloadIds = new HashMap<>();// < downloadid , messageid >

    public final static Set< String > LIST_MSG_TYPE;
    static
    {
        LIST_MSG_TYPE = new HashSet<>();

        LIST_MSG_TYPE.add(IM_DATATYPE_TEXT);
        LIST_MSG_TYPE.add(IM_DATATYPE_STICKER);
        LIST_MSG_TYPE.add(IM_DATATYPE_ANIMATED_STICKER);
        LIST_MSG_TYPE.add(IM_DATATYPE_IMAGE);
        LIST_MSG_TYPE.add(IM_DATATYPE_VIDEO);
        LIST_MSG_TYPE.add(IM_DATATYPE_MIC);
        LIST_MSG_TYPE.add(IM_DATATYPE_GIF);

    }


    public final static String IM_DATATYPE_GROUP_JOIN = "groupJoin";
    public final static String IM_DATATYPE_GROUP_LEAVE = "groupLeave";
    public final static String IM_DATATYPE_GROUP_KICK = "groupKick";
    public final static String IM_DATATYPE_GROUP_NAME = "groupName";
    public final static String IM_DATATYPE_GROUP_AVATAR = "groupAvatar";
    public final static String IM_DATATYPE_GROUP_CREATE = "groupCreate";
    public final static String IM_DATATYPE_CLIENT_JOIN = "clientJoin";
    public final static String IM_DATATYPE_CLIENT_UPDATE = "clientUpdate";
    public final static String IM_DATATYPE_CLIENT_STATUS = "clientStatus";
    public final static String IM_DATATYPE_CLIENT_GROUP_PERMISSION = "clientGroupPermission";

    public final static List< String > CG_LIST = Arrays.asList(IM_DATATYPE_GROUP_JOIN, IM_DATATYPE_GROUP_LEAVE, IM_DATATYPE_GROUP_NAME, IM_DATATYPE_GROUP_AVATAR, IM_DATATYPE_GROUP_CREATE);
    public final static String IM_DATATYPE_DELIVERY = "delivery";
    public final static String IM_DATATYPE_SEEN_DELIVERY = "seen";
    public final static String IM_DATATYPE_SEEN_TO_DELIVERY = "seenTo";

    public final static String IM_FROM_CLIENT = "c2c";
    public final static String IM_FROM_SERVER = "s2c";

    public final static String IM_RECEIVER_TYPE_GROUP_PUBLIC = "public";
    public final static String IM_RECEIVER_TYPE_GROUP_PRIVATE = "private";
    public final static String IM_RECEIVER_TYPE_CLIENT = "client";

    public final static String VIDEO_FILE = "ویدئو";
    public final static String IMAGE_FILE = "تصویر";
    public final static String STICKER = "برچسب";
    public final static String GROUP_MULTI_USER = "public";
    public final static String GROUP_SINGLE_USER = "private";
    public final static String AUDIO_FILE = "صوتی";

    public final static String Folder_Media = "Media";
    public final static String Folder_Image = "Image";
    public final static String Folder_Video = "Video";
    public final static String Folder_Audio = "Audio";
    public final static String Folder_Sent = "Sent";
    public final static String Folder_Thumb = ".Thumb";

    //public final static String MSG_SEP = "#";

    //public final static long SYNC_CONTACT_TIME = 100 * 60000;//per 100 minutes

    public final static String WS_URL = "http://api.gapafzar.com/v1";  //"http://api.30tel.com/v1"

    public final static String TAG = "gap1";

    //public static String GCM_SENDER_ID = "37688289869";

    public static final boolean IS_DEBUG=true;//Used in Loger Method in Helper Class

    public static final String REGEX_NOTEMPTY = "\\s+";

    public static final String gameWebServiceUrl = "http://brainteaser.appdana.com";
    public static final String handshakeUrl = "http://hs.brainteaser.appdana.com";
    public static final String gameWebServiceUrl2 = "http://brainteaser.tsitgames.com";
    public static final String handshakeUrl2 = "http://hs.brainteaser.tsitgames.com";


}
