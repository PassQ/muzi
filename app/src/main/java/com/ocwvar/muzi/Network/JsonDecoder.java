package com.ocwvar.muzi.Network;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.BusinessImageBean;
import com.ocwvar.muzi.Beans.OrderBean;
import com.ocwvar.muzi.Beans.PayRecordBean;
import com.ocwvar.muzi.Beans.PaymentBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.ApartmentBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.CommunityBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.RoomBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.UnitBean;
import com.ocwvar.muzi.Beans.RepireBean;
import com.ocwvar.muzi.Beans.RepireStatusBean;
import com.ocwvar.muzi.Beans.ReplyTabBean;
import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.Beans.UserType;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network
 * Date: 2016/5/4  17:15
 * Project: Muzi
 * 解析JSON数据的帮助类
 */

public class JsonDecoder {

    /**
     * 解析文章的Json数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 文章的列表。如果解析失败或文章数量为0，返回NULL
     */
    public static ArrayList<ArtclesBean> getArtclesJsonArray(String jsonString) {
        final String TAG = "Decoder_ArtclesJsonArray";

        //将获取到的字符串转换为Json数据

        JsonElement jsonElement;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, " JSON data is invaild");
            return null;
        }

        if (jsonElement != null) {

            //总文章数量
            int totalArtclesCount = 0;
            if (jsonElement.getAsJsonObject().get("Total") != null) {
                totalArtclesCount = jsonElement.getAsJsonObject().get("Total").getAsInt();
            }

            Log.d(TAG, "requested count:" + jsonArray.size());
            Log.d(TAG, "total count:" + totalArtclesCount);

            //如果文章数量大于 0
            if (jsonArray.size() > 0) {

                //创建存储的数组
                ArrayList<ArtclesBean> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    //遍历数组，生成文章的Bean对象

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    ArtclesBean artcle = new ArtclesBean();

                    //文章的添加时间
                    if (jsonObject.get("AddTime") != null && !TextUtils.isEmpty(jsonObject.get("AddTime").getAsString())) {
                        artcle.setAddTime(jsonObject.get("AddTime").getAsString());
                    } else {
                        artcle.setAddTime("");
                    }

                    //文章主体内容
                    if (jsonObject.get("Content") != null && !TextUtils.isEmpty(jsonObject.get("Content").getAsString())) {
                        String content = Jsoup.parse(jsonObject.get("Content").getAsString()).text();
                        if (content.length() > 40) {
                            artcle.setPreview(content.substring(0, 40));
                        } else {
                            artcle.setPreview(content);
                        }
                        artcle.setContent(jsonObject.get("Content").getAsString());
                    } else {
                        artcle.setContent("");
                        artcle.setPreview("");
                    }

                    //文章图片地址
                    if (jsonObject.get("RelativePath") != null && !TextUtils.isEmpty(jsonObject.get("RelativePath").getAsString())) {
                        String string = "";
                        if (!TextUtils.isEmpty(jsonObject.get("RelativePath").getAsString())) {
                            string = jsonObject.get("RelativePath").getAsString();
                            string = string.replaceFirst("\\\\", "/");
                            string = NetworkHelper.BASE_URL + string;
                        }
                        artcle.setRelativePath(string);
                    } else {
                        artcle.setRelativePath("");
                    }

                    //文章图片缩略图地址
                    if (jsonObject.get("RelativeThuPath") != null && !TextUtils.isEmpty(jsonObject.get("RelativeThuPath").getAsString())) {
                        String string = "";
                        if (!TextUtils.isEmpty(jsonObject.get("RelativeThuPath").getAsString())) {
                            //处理解析出来的字符串成网址

                            string = jsonObject.get("RelativeThuPath").getAsString();
                            string = string.replaceFirst("\\\\", "/");
                            string = NetworkHelper.BASE_URL + string;
                        }
                        artcle.setRelativeThuPath(string);
                    } else {
                        artcle.setRelativeThuPath("");
                    }

                    //文章标题
                    if (jsonObject.get("Title") != null && !TextUtils.isEmpty(jsonObject.get("Title").getAsString())) {
                        artcle.setTitle(jsonObject.get("Title").getAsString());
                    } else {
                        artcle.setTitle("");
                    }

                    if (i == 0) {
                        //如果是第一个item，则吧总文章数目放进去
                        artcle.setTotalArtclesCount(totalArtclesCount);
                    }
                    objects.add(artcle);
                }
                return objects;
            }
        }
        return null;
    }

    /**
     * 解析返回的状态信息
     *
     * @param jsonString Get到的Json数据文本
     * @return 解析完成后的数据，第一个元素是执行状态，第二个元素是执行返回的信息，如果解析数据失败，返回NULL
     */
    static String[] getStatusJsonData(String jsonString) {
        final String TAG = "Decoder_StatusJsonData";

        JsonElement jsonElement;
        JsonArray array;
        System.out.println(jsonString);

        try {

            jsonElement = new JsonParser().parse(jsonString);
            array = jsonElement.getAsJsonObject().get("resault").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, "Status JSON data is invaild");
            return null;
        }

        return new String[]{
                array.get(0).getAsJsonObject().get("state").getAsString()
                , array.get(1).getAsJsonObject().get("info").getAsString()
        };
    }

    /**
     * 解析返回的带ID状态信息
     *
     * @param jsonString Get到的Json数据文本
     * @return 解析完成后的数据，第一个元素是执行状态，第二个元素是执行返回的信息，如果解析数据失败，返回NULL
     */
    public static String[] getStatusIDJsonData(String jsonString) {
        final String TAG = "Decoder_StatusJsonData";

        JsonElement jsonElement;
        JsonArray array;
        System.out.println(jsonString);

        try {

            jsonElement = new JsonParser().parse(jsonString);
            array = jsonElement.getAsJsonObject().get("resault").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, "Status JSON data is invaild");
            return null;
        }

        return new String[]{
                array.get(0).getAsJsonObject().get("state").getAsString()
                , array.get(1).getAsJsonObject().get("ID").getAsString()
        };
    }

    /**
     * 解析用户信息
     *
     * @param jsonString Get到的Json数据文本
     * @return 解析完成后得到的UserBean。如果解析失败则返回NULL
     */
    public static UserBean getUserJsonData(String jsonString) {
        final String TAG = "Decoder_UserJsonData";

        JsonElement jsonElement;
        JsonObject infoObject;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            infoObject = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray().get(0).getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        UserBean userBean = new UserBean();

        /**
         * 最基础信息部分
         */

        //电话号码
        if (!isVaild(infoObject,"telephoneNum")) {
            return null;
        } else {
            userBean.setPhoneNumber(infoObject.get("telephoneNum").getAsString());
        }

        //用户名
        if (!isVaild(infoObject,"name")) {
            //如果昵称为空 , 则使用电话号码作为用户名
            userBean.setUserName(userBean.getPhoneNumber());
        } else {
            userBean.setUserName(infoObject.get("name").getAsString());
        }

        //登录密码
        if (!isVaild(infoObject,"loginPSW")) {
            return null;
        } else {
            userBean.setPassword(infoObject.get("loginPSW").getAsString());
        }

        //登录ID
        if (!isVaild(infoObject,"loginID")) {
            return null;
        } else {
            userBean.setLoginID(infoObject.get("loginID").getAsString());
        }

        //用户ID
        if (!isVaild(infoObject,"ID")) {
            return null;
        } else {
            userBean.setUserID(infoObject.get("ID").getAsString());
        }

        //头像
        if (!isVaild(infoObject,"RelativePath")) {
            userBean.setUserHeadImage("");
        } else {
            String temp = infoObject.get("RelativePath").getAsString();
            try {
                temp = temp.replaceFirst("\\\\", "/");
                temp = NetworkHelper.BASE_URL + temp;
                System.out.println(temp);
                userBean.setUserHeadImage(temp);
            } catch (Exception e) {
                //如果在替换字符的时候出现了错误 , 则头像地址为空
                userBean.setUserHeadImage("");
            }
        }

        //用户身份
        if (infoObject.get("isServiceman") == null || TextUtils.isEmpty(infoObject.get("isServiceman").getAsString())) {
            return null;
        } else {
            if (infoObject.get("isServiceman").getAsInt() == 1) {
                userBean.setUserType(UserType.SERVICE_MAN);
            }
        }

        if (userBean.getUserType() != null && userBean.getUserType() == UserType.SERVICE_MAN){
            //如果用户是维修人员，则需要跳过某些信息数据

            userBean.setCommunityName("无");
            userBean.setCommunityID("1");
            userBean.setApartment("无");
            userBean.setRoom("1");
            userBean.setRoomName("无");
            userBean.setUnit("1");
            userBean.setUnitName("无");

        }else {

            if (!isVaild(infoObject,"CommunityName")) {
                userBean.setCommunityName("小区名称未定义");
            } else {
                userBean.setCommunityName(infoObject.get("CommunityName").getAsString());
            }

            if (!isVaild(infoObject,"communityID")) {
                return null;
            } else {
                userBean.setCommunityID(infoObject.get("communityID").getAsString());
            }

            if (!isVaild(infoObject,"addTime")) {
                return null;
            } else {
                userBean.setRegisterTime(infoObject.get("addTime").getAsString());
            }

            if (!isVaild(infoObject,"ApartmentName")) {
                return null;
            } else {
                userBean.setApartment(infoObject.get("ApartmentName").getAsString());
            }

            if (!isVaild(infoObject,"room")) {
                return null;
            } else {
                userBean.setRoom(infoObject.get("room").getAsString());
            }

            if (!isVaild(infoObject,"RoomName")) {
                return null;
            } else {
                userBean.setRoomName(infoObject.get("RoomName").getAsString());
            }

            if (!isVaild(infoObject,"unit")) {
                return null;
            } else {
                userBean.setUnit(infoObject.get("unit").getAsString());
            }

            if (!isVaild(infoObject,"UnitName")) {
                return null;
            } else {
                userBean.setUnitName(infoObject.get("UnitName").getAsString());
            }

            if (!isVaild(infoObject,"isAuthentication")) {
                return null;
            } else {
                if (infoObject.get("isAuthentication").getAsInt() == 1) {
                    userBean.setUserType(UserType.NORMAL);
                } else {
                    userBean.setUserType(UserType.GUEST);
                }
            }

        }



        return userBean;
    }

    /**
     * 解析位置数据
     *
     * @param jsonString Get到的Json数据文本
     * @param dataType   数据的类型 : 小区  楼号  单元  房号
     * @param datasList  要存放数据的 ArrayList
     * @return 解析状态
     */
    public static boolean getPositionDatas(String jsonString, NetworkHelper.DataType dataType, ArrayList datasList) {
        final String TAG = "Decoder_PositionDatas";

        JsonElement jsonElement;
        JsonArray datasJsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            datasJsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return false;
        }

        for (int i = 0; i < datasJsonArray.size(); i++) {
            JsonObject jsonObject = datasJsonArray.get(i).getAsJsonObject();

            switch (dataType) {
                case String_Array_RepireType:
                    String[] strings = new String[2];
                    if (jsonObject.get("ID") == null) continue;
                    else {
                        strings[0] = jsonObject.get("ID").getAsString();
                    }

                    if (jsonObject.get("name") == null) continue;
                    else {
                        strings[1] = jsonObject.get("name").getAsString();
                    }

                    datasList.add(strings);
                    break;
                case LIST_community:

                    CommunityBean community;
                    if (jsonObject.get("ID") == null) continue;
                    else {
                        community = new CommunityBean();
                        community.setID(jsonObject.get("ID").getAsString());
                    }

                    if (jsonObject.get("belongArea") == null || TextUtils.isEmpty(jsonObject.get("belongArea").getAsString())) {
                        community.setBelongArea("所属区域未定义");
                    } else {
                        community.setBelongArea(jsonObject.get("belongArea").getAsString());
                    }

                    if (jsonObject.get("isHQ") == null || TextUtils.isEmpty(jsonObject.get("isHQ").getAsString())) {
                        community.setHQ(false);
                    } else if (jsonObject.get("isHQ").getAsString().toLowerCase().equals("false")) {
                        community.setHQ(false);
                    } else if (jsonObject.get("isHQ").getAsString().toLowerCase().equals("true")) {
                        community.setHQ(true);
                    } else {
                        community.setHQ(false);
                    }

                    if (jsonObject.get("address") == null || TextUtils.isEmpty(jsonObject.get("address").getAsString())) {
                        community.setAddress("地址未定义");
                    } else {
                        community.setAddress(jsonObject.get("address").getAsString());
                    }

                    if (jsonObject.get("name") == null || TextUtils.isEmpty(jsonObject.get("name").getAsString())) {
                        community.setName("名称未定义");
                    } else {
                        community.setName(jsonObject.get("name").getAsString());
                    }

                    if (jsonObject.get("telephone") == null || TextUtils.isEmpty(jsonObject.get("telephone").getAsString())) {
                        community.setTelephone("电话未定义");
                    } else {
                        community.setTelephone(jsonObject.get("telephone").getAsString());
                    }

                    datasList.add(community);

                    break;
                case LIST_apartment:

                    ApartmentBean apartment;
                    if (jsonObject.get("ID") == null) continue;
                    else {
                        apartment = new ApartmentBean();
                        apartment.setId(jsonObject.get("ID").getAsString());
                    }

                    if (jsonObject.get("communityID") == null || TextUtils.isEmpty(jsonObject.get("communityID").getAsString())) {
                        apartment.setCommunityID("所属小区未定义");
                    } else {
                        apartment.setCommunityID(jsonObject.get("communityID").getAsString());
                    }

                    if (jsonObject.get("name") == null || TextUtils.isEmpty(jsonObject.get("name").getAsString())) {
                        apartment.setName("楼号栋数未定义");
                    } else {
                        apartment.setName(jsonObject.get("name").getAsString());
                    }

                    datasList.add(apartment);
                    break;
                case LIST_unit:

                    UnitBean unit;
                    if (jsonObject.get("ID") == null) continue;
                    else {
                        unit = new UnitBean();
                        unit.setId(jsonObject.get("ID").getAsString());
                    }

                    if (jsonObject.get("apartmentID") == null || TextUtils.isEmpty(jsonObject.get("apartmentID").getAsString())) {
                        unit.setApartmentID("所属楼号未定义");
                    } else {
                        unit.setApartmentID(jsonObject.get("apartmentID").getAsString());
                    }

                    if (jsonObject.get("name") == null || TextUtils.isEmpty(jsonObject.get("name").getAsString())) {
                        unit.setName("单元名称未定义");
                    } else {
                        unit.setName(jsonObject.get("name").getAsString());
                    }

                    datasList.add(unit);
                    break;
                case LIST_room:

                    RoomBean room;
                    if (jsonObject.get("ID") == null) continue;
                    else {
                        room = new RoomBean();
                        room.setId(jsonObject.get("ID").getAsString());
                    }

                    if (jsonObject.get("unitID") == null || TextUtils.isEmpty(jsonObject.get("unitID").getAsString())) {
                        room.setUnitID("所属楼号未定义");
                    } else {
                        room.setUnitID(jsonObject.get("unitID").getAsString());
                    }

                    if (jsonObject.get("name") == null || TextUtils.isEmpty(jsonObject.get("name").getAsString())) {
                        room.setName("单元名称未定义");
                    } else {
                        room.setName(jsonObject.get("name").getAsString());
                    }

                    datasList.add(room);

                    break;
                default:
                    continue;
            }

        }
        return true;
    }

    /**
     * 解析报修状态数据
     *
     * @param jsonString       Get到的Json数据文本
     * @param repireStatusBeen 要存放数据的 ArrayList
     * @return 解析状态
     */
    public static boolean getRepireStatus(String jsonString, ArrayList<RepireStatusBean> repireStatusBeen) {
        final String TAG = "Decoder_RepireStatus";

        JsonElement jsonElement;
        JsonArray datasJsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            datasJsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return false;
        }

        for (int i = 0; i < datasJsonArray.size(); i++) {
            JsonObject jsonObject = datasJsonArray.get(i).getAsJsonObject();
            RepireStatusBean repireStatusBean = new RepireStatusBean();

            if (jsonObject.get("ID") == null || TextUtils.isEmpty(jsonObject.get("ID").getAsString())) {
                continue;
            } else {
                repireStatusBean.setID(jsonObject.get("ID").getAsString());
            }

            if (jsonObject.get("finishServiceTime") == null || TextUtils.isEmpty(jsonObject.get("finishServiceTime").getAsString())) {
                repireStatusBean.setFinishServiceTime("尚未维修");
            } else {
                repireStatusBean.setFinishServiceTime(jsonObject.get("finishServiceTime").getAsString());
            }

            if (jsonObject.get("location") == null || TextUtils.isEmpty(jsonObject.get("location").getAsString())) {
                repireStatusBean.setLocation("地址未设置");
            } else {
                repireStatusBean.setLocation(jsonObject.get("location").getAsString());
            }

            if (jsonObject.get("XM") == null || TextUtils.isEmpty(jsonObject.get("XM").getAsString())) {
                repireStatusBean.setServicemanName("维修人员名字未定义");
            } else {
                repireStatusBean.setServicemanName(jsonObject.get("XM").getAsString());
            }

            if (jsonObject.get("servicemanID") == null || TextUtils.isEmpty(jsonObject.get("servicemanID").getAsString())) {
                repireStatusBean.setServicemanID("");
            } else {
                repireStatusBean.setServicemanID(jsonObject.get("servicemanID").getAsString());
            }

            if (jsonObject.get("name") == null || TextUtils.isEmpty(jsonObject.get("name").getAsString())) {
                repireStatusBean.setServiceType("报修类型");
            } else {
                repireStatusBean.setServiceType(jsonObject.get("name").getAsString());
            }

            if (jsonObject.get("submitTime") == null || TextUtils.isEmpty(jsonObject.get("submitTime").getAsString())) {
                repireStatusBean.setSubmitTime("申请时间不祥");
            } else {
                repireStatusBean.setSubmitTime(jsonObject.get("submitTime").getAsString());
            }

            if (jsonObject.get("servicemanTel") == null || TextUtils.isEmpty(jsonObject.get("servicemanTel").getAsString())) {
                repireStatusBean.setServicemanTel("无维修人员电话");
            } else {
                repireStatusBean.setServicemanTel(jsonObject.get("servicemanTel").getAsString());
            }

            if (jsonObject.get("Content") == null || TextUtils.isEmpty(jsonObject.get("Content").getAsString())) {
                repireStatusBean.setContext("无报修描述内容");
            } else {
                final String context = jsonObject.get("Content").getAsString();
                repireStatusBean.setContext(context);
                if (context.length() >= 5) {
                    repireStatusBean.setTitle(context.substring(0, 5) + "...");
                } else {
                    repireStatusBean.setTitle(context);
                }

            }

            if (jsonObject.get("currentState") == null || TextUtils.isEmpty(jsonObject.get("currentState").getAsString())) {
                repireStatusBean.setCurrentState("0");
            } else {
                repireStatusBean.setCurrentState(jsonObject.get("currentState").getAsString());
            }

            if (isVaild(jsonObject, "AttrIDs_P")) {
                String ids = jsonObject.get("AttrIDs_P").getAsString();

                if (ids.contains(",")) {
                    //如果图像id多于一张
                    try {
                        final String[] idsString = ids.split(",");
                        //设置每张图片的ID , 再根据id的数量来创建存储图片地址的数组长度
                        repireStatusBean.setPicturesID(idsString);
                        repireStatusBean.setPictures(new String[idsString.length]);
                        repireStatusBean.setPicturesThu(new String[idsString.length]);
                    } catch (Exception e) {
                        //如果正则处理出错 , 则不设置图片
                        repireStatusBean.setPicturesID(null);
                    }
                } else {
                    //当id只有一条的情况下
                    repireStatusBean.setPicturesID(new String[]{ids});
                    repireStatusBean.setPictures(new String[1]);
                    repireStatusBean.setPicturesThu(new String[1]);
                }

            } else {
                repireStatusBean.setPicturesID(null);
            }

            repireStatusBeen.add(repireStatusBean);
        }
        return true;
    }

    /**
     * 解析缴费项目
     *
     * @param jsonString  Get到的Json数据文本
     * @param paymentList 要存放数据的 ArrayList
     * @return 解析状态
     */
    public static boolean getPaymentListDatas(String jsonString, ArrayList<PaymentBean> paymentList) {
        final String TAG = "Decoder_PaymentListDatas";

        JsonElement jsonElement;
        JsonArray datasJsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            datasJsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return false;
        }

        for (int i = 0; i < datasJsonArray.size(); i++) {
            JsonObject jsonObject = datasJsonArray.get(i).getAsJsonObject();
            PaymentBean paymentBean = new PaymentBean();

            //如果    项目ID无效  则当前缴费项目无效
            if (jsonObject.get("ID") == null || TextUtils.isEmpty(jsonObject.get("ID").getAsString())) {
                continue;
            } else {
                paymentBean.setID(jsonObject.get("ID").getAsString());
            }

            //如果    项目单价无效  则当前缴费项目无效
            if (jsonObject.get("singlePrice") == null || TextUtils.isEmpty(jsonObject.get("singlePrice").getAsString())) {
                continue;
            } else {
                paymentBean.setSinglePrice(jsonObject.get("singlePrice").getAsString());
            }

            //如果    项目缴费房号无效    则当前缴费项目无效
            if (jsonObject.get("roomID") == null || TextUtils.isEmpty(jsonObject.get("roomID").getAsString())) {
                paymentBean.setRoomID("缴费项目未定义");
            } else {
                paymentBean.setRoomID(jsonObject.get("roomID").getAsString());
            }

            if (jsonObject.get("sumsBalance") == null || TextUtils.isEmpty(jsonObject.get("sumsBalance").getAsString())) {
                paymentBean.setSumsBalance("无数据");
            } else {
                paymentBean.setSumsBalance(jsonObject.get("sumsBalance").getAsString());
            }

            if (jsonObject.get("name") == null || TextUtils.isEmpty(jsonObject.get("name").getAsString())) {
                paymentBean.setName("缴费项目未定义");
            } else {
                paymentBean.setName(jsonObject.get("name").getAsString());
            }

            paymentList.add(paymentBean);
        }

        return true;
    }

    /**
     * 解析推送消息内的数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据数组  0:文章ID   1:GroupID .若数据不合法解析失败或不是文章数据 , 则返回NULL
     */
    public static String[] getPushMessageData(String jsonString) {
        final String TAG = "Decoder_PushMessageData";

        JsonElement jsonElement;
        JsonObject jsonObject;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        String[] datas = new String[2];
        if (jsonObject.get("type") == null || TextUtils.isEmpty(jsonObject.get("type").getAsString())) {
            return null;
        } else {
            if (!jsonObject.get("type").getAsString().toLowerCase().equals("article")) {
                //如果推送的数据类型不是文章 , 则不解析
                return null;
            }
        }

        if (jsonObject.get("id") == null || TextUtils.isEmpty(jsonObject.get("id").getAsString())) {
            return null;
        } else {
            datas[0] = jsonObject.get("id").getAsString();
        }

        if (jsonObject.get("groupid") == null || TextUtils.isEmpty(jsonObject.get("groupid").getAsString())) {
            datas[1] = "";
        } else {
            datas[1] = jsonObject.get("groupid").getAsString();
        }

        return datas;

    }

    /**
     * 解析缴费列表数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 解析状态
     */
    public static boolean getPayRecordDatas(String jsonString, ArrayList<PayRecordBean> source) {
        final String TAG = "Decoder_PayRecordDatas";

        JsonElement jsonElement;
        JsonArray datasJsonArray;

        String totalCount;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            datasJsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();
            totalCount = jsonElement.getAsJsonObject().get("Total").getAsString();
            System.out.println("缴费记录总数: " + totalCount);
        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return false;
        }

        for (int i = 0; i < datasJsonArray.size(); i++) {
            JsonObject jsonObject = datasJsonArray.get(i).getAsJsonObject();
            PayRecordBean bean = new PayRecordBean();

            if (i == 0) {
                bean.setTotalCount(totalCount);
            }

            if (jsonObject.get("name") == null || TextUtils.isEmpty(jsonObject.get("name").getAsString())) {
                bean.setName("缴费项目名未定义");
            } else {
                bean.setName(jsonObject.get("name").getAsString());
            }

            if (jsonObject.get("chargeSum") == null || TextUtils.isEmpty(jsonObject.get("chargeSum").getAsString())) {
                bean.setChargeSum("充值金额获取失败");
            } else {
                bean.setChargeSum(jsonObject.get("chargeSum").getAsString());
            }

            if (jsonObject.get("chargeTime") == null || TextUtils.isEmpty(jsonObject.get("chargeTime").getAsString())) {
                bean.setChargeTime("充值时间未定义");
            } else {
                bean.setChargeTime(jsonObject.get("chargeTime").getAsString());
            }

            if (jsonObject.get("orderNo") == null || TextUtils.isEmpty(jsonObject.get("orderNo").getAsString())) {
                bean.setOrderNo("订单号未定义");
            } else {
                bean.setOrderNo(jsonObject.get("orderNo").getAsString());
            }

            source.add(bean);

        }

        return true;

    }

    /**
     * 解析维修信息数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 解析得到的数据列表
     */
    public static ArrayList<RepireBean> getRepireList(String jsonString) {
        JsonArray jsonArray;
        int totalCount = -1;

        try {
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();
            totalCount = jsonElement.getAsJsonObject().get("Total").getAsInt();
        } catch (Exception e) {
            return null;
        }

        if (jsonArray != null && jsonArray.size() > 0) {
            ArrayList<RepireBean> repireBeen = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                RepireBean bean = new RepireBean();

                if (isVaild(object, "reserveTime")) {
                    bean.setReserveTime(object.get("reserveTime").getAsString());
                } else {
                    bean.setReserveTime("未定义");
                }

                if (isVaild(object, "proprietorID")) {
                    bean.setProprietorID(object.get("proprietorID").getAsString());
                } else {
                    bean.setProprietorID("未定义");
                }

                if (isVaild(object, "remark")) {
                    bean.setRemark(object.get("remark").getAsString());
                } else {
                    bean.setRemark("未定义");
                }

                if (isVaild(object, "principalAcceptTime")) {
                    bean.setPrincipalAcceptTime(object.get("principalAcceptTime").getAsString());
                } else {
                    bean.setPrincipalAcceptTime("未定义");
                }

                if (isVaild(object, "evaluateReson")) {
                    bean.setEvaluateReson(object.get("evaluateReson").getAsString());
                } else {
                    bean.setEvaluateReson("未定义");
                }

                if (isVaild(object, "ID")) {
                    bean.setID(object.get("ID").getAsString());
                } else {
                    bean.setID("未定义");
                }

                if (isVaild(object, "servicemanID")) {
                    bean.setServicemanID(object.get("servicemanID").getAsString());
                } else {
                    bean.setServicemanID("未定义");
                }

                if (isVaild(object, "AttrIDs_O")) {
                    bean.setAttrIDs_O(object.get("AttrIDs_O").getAsString());
                } else {
                    bean.setAttrIDs_O("未定义");
                }

                if (isVaild(object, "acceptDirectorID")) {
                    bean.setAcceptDirectorID(object.get("acceptDirectorID").getAsString());
                } else {
                    bean.setAcceptDirectorID("未定义");
                }

                if (isVaild(object, "serviceTypeID")) {
                    bean.setServiceTypeID(object.get("serviceTypeID").getAsString());
                } else {
                    bean.setServiceTypeID("未定义");
                }

                if (isVaild(object, "overdueTime")) {
                    bean.setOverdueTime(object.get("overdueTime").getAsString());
                } else {
                    bean.setOverdueTime("未定义");
                }

                if (isVaild(object, "location")) {
                    bean.setLocation(object.get("location").getAsString());
                } else {
                    bean.setLocation("未定义");
                }

                if (isVaild(object, "currentState")) {
                    bean.setCurrentState(object.get("currentState").getAsString());
                } else {
                    bean.setCurrentState("未定义");
                }

                if (isVaild(object, "evaluateTime")) {
                    bean.setEvaluateTime(object.get("evaluateTime").getAsString());
                } else {
                    bean.setEvaluateTime("未定义");
                }

                if (isVaild(object, "evaluateResault")) {
                    bean.setEvaluateResault(object.get("evaluateResault").getAsString());
                } else {
                    bean.setEvaluateResault("未定义");
                }

                if (isVaild(object, "servicemanAcceptTime")) {
                    bean.setServicemanAcceptTime(object.get("servicemanAcceptTime").getAsString());
                } else {
                    bean.setServicemanAcceptTime("未定义");
                }

                if (isVaild(object, "finishServiceTime")) {
                    bean.setFinishServiceTime(object.get("finishServiceTime").getAsString());
                } else {
                    bean.setFinishServiceTime("未定义");
                }

                if (isVaild(object, "communityID")) {
                    bean.setCommunityID(object.get("communityID").getAsString());
                } else {
                    bean.setCommunityID("未定义");
                }

                if (isVaild(object, "submitTime")) {
                    bean.setSubmitTime(object.get("submitTime").getAsString());
                } else {
                    bean.setSubmitTime("未定义");
                }

                if (isVaild(object, "proprietorTel")) {
                    bean.setProprietorTel(object.get("proprietorTel").getAsString());
                } else {
                    bean.setProprietorTel("未定义");
                }

                if (isVaild(object, "compulsionTime")) {
                    bean.setCompulsionTime(object.get("compulsionTime").getAsString());
                } else {
                    bean.setCompulsionTime("未定义");
                }

                if (isVaild(object, "Content")) {
                    final String content = object.get("Content").getAsString();
                    bean.setContent(content);
                    if (content.length() >= 5) {
                        bean.setTitle(content.substring(0, 5) + "...");
                    } else {
                        bean.setTitle(content);
                    }
                } else {
                    bean.setContent("未定义");
                }

                if (isVaild(object, "servicemanTel")) {
                    bean.setServicemanTel(object.get("servicemanTel").getAsString());
                } else {
                    bean.setServicemanTel("未定义");
                }

                if (isVaild(object, "finalServicemanID")) {
                    bean.setFinalServicemanID(object.get("finalServicemanID").getAsString());
                } else {
                    bean.setFinalServicemanID("未定义");
                }

                if (isVaild(object, "directorAcceptTime")) {
                    bean.setDirectorAcceptTime(object.get("directorAcceptTime").getAsString());
                } else {
                    bean.setDirectorAcceptTime("未定义");
                }

                if (isVaild(object, "submitCounts")) {
                    bean.setSubmitCounts(object.get("submitCounts").getAsString());
                } else {
                    bean.setSubmitCounts("未定义");
                }

                if (isVaild(object, "groupID")) {
                    bean.setGroupID(object.get("groupID").getAsString());
                } else {
                    bean.setGroupID("未定义");
                }

                if (isVaild(object, "preServicemanID")) {
                    bean.setPreServicemanID(object.get("preServicemanID").getAsString());
                } else {
                    bean.setPreServicemanID("未定义");
                }

                if (isVaild(object, "AttrIDs_P")) {
                    String ids = object.get("AttrIDs_P").getAsString();
                    bean.setAttrIDs_P(ids);

                    if (ids.contains(",")) {
                        //如果图像id多于一张
                        try {
                            bean.setPicturesID(ids.split(","));
                        } catch (Exception e) {
                            //如果正则处理出错 , 则不设置图片
                            bean.setPicturesID(null);
                        }
                    } else {
                        bean.setPicturesID(new String[]{ids});
                    }

                } else {
                    bean.setAttrIDs_P("未定义");
                }

                //根据图片id的数量来创建图片地址和图片缩略图的预留位置
                if (bean.getPicturesID() != null) {
                    bean.setPictures(new String[bean.getPicturesID().length]);
                    bean.setPicturesThu(new String[bean.getPicturesID().length]);
                }

                String preview = bean.getContent();
                if (preview.length() >= 20) {
                    preview = preview.substring(0, 20);
                }
                bean.setPreview(preview);

                object = null;
                repireBeen.add(0, bean);

            }

            jsonArray = null;
            repireBeen.get(0).setTotalCount(totalCount);
            return repireBeen;
        }

        return null;
    }

    /**
     * 解析图片地址
     *
     * @param jsonString Get到的Json数据文本
     * @return 解析得到的数据数组
     */
    public static String[] getPicturesUrl(String jsonString) {
        final String picHead = NetworkHelper.BASE_URL;
        final String TAG = "getPicturesUrl";


        JsonObject jsonObject;

        try {

            JsonElement jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonArray().get(0).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }

        String[] urls = new String[2];

        //解析预览图
        if (isVaild(jsonObject, "RelativeThuPath")) {
            urls[0] = jsonObject.get("RelativeThuPath").getAsString();
            urls[0] = picHead + urls[0].replaceFirst("\\\\", "/");
        } else {
            urls[0] = "";
        }

        //解析大图
        if (isVaild(jsonObject, "RelativePath")) {
            urls[1] = jsonObject.get("RelativePath").getAsString();
            urls[1] = picHead + urls[1].replaceFirst("\\\\", "/");
        } else {
            urls[1] = "";
        }


        Log.d(TAG, "RelativeThuPath:" + urls[0]);
        Log.d(TAG, "RelativePath:" + urls[1]);
        Log.d(TAG, "--------");
        return urls;
    }


    /**
     * 解析获取签到历史数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据数组  签到日期数组
     */
    public static List<String> getSignHistoryData(String jsonString) {
        final String TAG = "Decoder_SignHistoryData";

        JsonElement jsonElement;
        JsonObject jsonObject;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();
            jsonArray=jsonObject.get("dateArray").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        List<String> dateList=new ArrayList<>();

        if (jsonArray==null||"".equals(jsonArray)) {
            return  null;

        } else {

            for (int i = 0; i <jsonArray.size(); i++) {
                dateList.add(jsonArray.get(i).toString());
            }

        }




        return dateList;

    }

    /**
     * 解析签到返回数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据数组  data[0]签到信息 data[1]签到增加积分
     */
    public static String[] upSignData(String jsonString) {
        final String TAG = "Decoder_UpSignData";

        JsonElement jsonElement;
        JsonObject jsonObject;



        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        String [] data=new String[2];

        if (isVaild(jsonObject,"message")) {
            data[0]=jsonObject.get("message").getAsString();
        } else {
            data[0]=null;
        }
        if(isVaild(jsonObject,"score")){
            data[1]=jsonObject.get("score").getAsString();
        }else {
            data[1]=null;
        }




        return data;

    }
    /**
     * 解析获取到的积分数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据字符串  总积分
     */
    public static String getScoreData(String jsonString) {
        final String TAG = "Decoder_GetScoreData";

        JsonElement jsonElement;
        JsonObject jsonObject;



        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        String data;

        if (isVaild(jsonObject,"scoreSum")) {
            data=jsonObject.get("scoreSum").getAsString();
        } else {
            data=null;
        }





        return data;

    }

    /**
     * 解析点赞返回数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据数组  data[0]签到是否成功 data[1]签到增加积分
     */
    public static String[] upLikeData(String jsonString) {
        final String TAG = "Decoder_UpLikeData";

        JsonElement jsonElement;
        JsonObject jsonObject;



        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        String [] data=new String[2];
        if(jsonObject.get("isSuccessed").getAsBoolean()){
            data[0] = "点赞成功！";
            data[1] = jsonObject.get("score").getAsString();
        }else {
            return null;
        }






        return data;

    }


    /**
     * 解析商家列表的Json数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 商家的列表。如果解析失败或文章数量为0，返回NULL
     */
    public static ArrayList<BulletinBean> getBusinessJsonArray(String jsonString) {
        final String TAG = "Decoder_BusinessJsonArray";

        //将获取到的字符串转换为Json数据

        JsonElement jsonElement;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, " JSON data is invaild");
            return null;
        }

        if (jsonElement != null) {

            //总文章数量
            int totalArtclesCount = 0;
            if (jsonElement.getAsJsonObject().get("Total") != null) {
                totalArtclesCount = jsonElement.getAsJsonObject().get("Total").getAsInt();
            }

            Log.d(TAG, "requested count:" + jsonArray.size());
            Log.d(TAG, "total count:" + totalArtclesCount);

            //如果文章数量大于 0
            if (jsonArray.size() > 0) {

                //创建存储的数组
                ArrayList<BulletinBean> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    //遍历数组，生成文章的Bean对象

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    BulletinBean bulletin = new BulletinBean();

                    //列表的标题
                    if (jsonObject.get("storeName") != null && !TextUtils.isEmpty(jsonObject.get("storeName").getAsString())) {
                        bulletin.setTitle(jsonObject.get("storeName").getAsString());
                    } else {
                        bulletin.setTitle("");
                    }

                    //列表的介绍
                    if (jsonObject.get("storeMsg") != null && !TextUtils.isEmpty(jsonObject.get("storeMsg").getAsString())) {

                        bulletin.setIntroduce(jsonObject.get("storeMsg").getAsString());
                    } else {
                        bulletin.setIntroduce("");
                    }

                    //列表的状态
                    if (jsonObject.get("isOpen") != null) {

                        bulletin.setStatus(jsonObject.get("isOpen").getAsInt());
                    } else {
                        bulletin.setStatus(-1);
                    }



                    //列表图片地址
                    if (jsonObject.get("storeHeadURL") != null && !TextUtils.isEmpty(jsonObject.get("storeHeadURL").getAsString())) {
                        String string = "";
                        if (!TextUtils.isEmpty(jsonObject.get("storeHeadURL").getAsString())) {
                            string = jsonObject.get("storeHeadURL").getAsString();
                            string = string.replaceFirst("\\\\", "/");
                            string = NetworkHelper.BASE_URL + string;
                        }
                        bulletin.setImagePath(string);
                    } else {
                        bulletin.setImagePath("");
                    }

                    //商家(周边)编号
                    if (jsonObject.get("storeID") != null) {

                        bulletin.setBusinessID(jsonObject.get("storeID").getAsInt());
                    } else {
                        bulletin.setBusinessID(-1);
                    }

                    //商家评分
                    if (jsonObject.get("rate") != null && !TextUtils.isEmpty(jsonObject.get("rate").getAsString())) {

                        bulletin.setScore(jsonObject.get("rate").getAsString());
                    } else {
                        bulletin.setScore("");
                    }
                    //商家地址
                    if (jsonObject.get("address") != null && !TextUtils.isEmpty(jsonObject.get("address").getAsString())) {
                        bulletin.setAddress(jsonObject.get("address").getAsString());
                    } else {
                        bulletin.setAddress("");
                    }
                    //商家联系方式
                    if (jsonObject.get("storeTel") != null && !TextUtils.isEmpty(jsonObject.get("storeTel").getAsString())) {
                        bulletin.setPhoneNum(jsonObject.get("storeTel").getAsString());
                    } else {
                        bulletin.setPhoneNum("");
                    }
                    //商家开店时间
                    if (jsonObject.get("startTime") != null && !TextUtils.isEmpty(jsonObject.get("startTime").getAsString())) {
                        bulletin.setOpenTime(jsonObject.get("startTime").getAsString());
                    } else {
                        bulletin.setOpenTime("");
                    }
                    //商家闭店时间
                    if (jsonObject.get("endTime") != null && !TextUtils.isEmpty(jsonObject.get("endTime").getAsString())) {
                        bulletin.setEndTime(jsonObject.get("endTime").getAsString());
                    } else {
                        bulletin.setEndTime("");
                    }

                    if (jsonObject.get("offDays") != null ) {

                        final int[] offDays = new int[]{0, 0, 0, 0, 0, 0, 0};
                        final StringBuilder stringBuilder = new StringBuilder();
                        String str =jsonObject.get("offDays").getAsString().replaceAll(" ","").substring(1,14);

                        String[] array = str.split(",");
                        for (int i1 = 0; i1 < array.length; i1++) {
                            //数组部分
                            offDays[i1] = Integer.parseInt(array[i1]);

                            //文字部分
                            if (offDays[i1] == 1) {
                                switch (i1) {
                                    case 0:
                                        stringBuilder.append("周一  ");
                                        break;
                                    case 1:
                                        stringBuilder.append("周二  ");
                                        break;
                                    case 2:
                                        stringBuilder.append("周三  ");
                                        break;
                                    case 3:
                                        stringBuilder.append("周四  ");
                                        break;
                                    case 4:
                                        stringBuilder.append("周五  ");
                                        break;
                                    case 5:
                                        stringBuilder.append("周六  ");
                                        break;
                                    case 6:
                                        stringBuilder.append("周日");
                                        break;
                                }

                            }
                        }
                        bulletin.setOffdays(String.valueOf(stringBuilder));
                    }else {
                        bulletin.setOffdays("");
                    }

                        if (i == 0) {
                        //如果是第一个item，则吧总文章数目放进去
                        bulletin.setTotalCount(totalArtclesCount);
                    }
                    objects.add(bulletin);
                }
                return objects;
            }
        }
        return null;
    }

    /**
     * 解析周边列表的Json数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 商家的列表。如果解析失败或文章数量为0，返回NULL
     */
    public static ArrayList<BulletinBean> getCircumJsonArray(String jsonString) {
        final String TAG = "Decoder_CircumJsonArray";

        //将获取到的字符串转换为Json数据

        JsonElement jsonElement;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, " JSON data is invaild");
            return null;
        }

        if (jsonElement != null) {

            //总文章数量
            int totalArtclesCount = 0;
            if (jsonElement.getAsJsonObject().get("Total") != null) {
                totalArtclesCount = jsonElement.getAsJsonObject().get("Total").getAsInt();
            }

            Log.d(TAG, "requested count:" + jsonArray.size());
            Log.d(TAG, "total count:" + totalArtclesCount);

            //如果文章数量大于 0
            if (jsonArray.size() > 0) {

                //创建存储的数组
                ArrayList<BulletinBean> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    //遍历数组，生成文章的Bean对象

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    BulletinBean bulletin = new BulletinBean();

                    //列表的标题
                    if (jsonObject.get("circumName") != null && !TextUtils.isEmpty(jsonObject.get("circumName").getAsString())) {
                        bulletin.setTitle(jsonObject.get("circumName").getAsString());
                    } else {
                        bulletin.setTitle("");
                    }

                    //列表的介绍
                    if (jsonObject.get("circumIntroduce") != null && !TextUtils.isEmpty(jsonObject.get("circumIntroduce").getAsString())) {

                        bulletin.setIntroduce(jsonObject.get("circumIntroduce").getAsString());
                    } else {
                        bulletin.setIntroduce("");
                    }

                    //列表的电话号码
                    if (jsonObject.get("circumTel") != null && !TextUtils.isEmpty(jsonObject.get("circumTel").getAsString())) {

                        bulletin.setPhoneNum(jsonObject.get("circumTel").getAsString());
                    } else {
                        bulletin.setPhoneNum("");
                    }

                    //列表图片地址
                    if (jsonObject.get("circumPortrait") != null && !TextUtils.isEmpty(jsonObject.get("circumPortrait").getAsString())) {
                        String string = "";
                        if (!TextUtils.isEmpty(jsonObject.get("circumPortrait").getAsString())) {
                            string = jsonObject.get("circumPortrait").getAsString();
                            string = string.replaceFirst("\\\\", "/");
                            string = NetworkHelper.BASE_URL + string;
                        }
                        bulletin.setImagePath(string);
                    } else {
                        bulletin.setImagePath("");
                    }

                    //详情图片地址
                    if (jsonObject.get("pictureID") != null && !TextUtils.isEmpty(jsonObject.get("pictureID").getAsString())) {
                        String string = "";
                        if (!TextUtils.isEmpty(jsonObject.get("pictureID").getAsString())) {
                            string = jsonObject.get("pictureID").getAsString();
                            string = string.replaceFirst("\\\\", "/");
                            string = NetworkHelper.BASE_URL + string;
                        }
                        bulletin.setDetailedImagePath(string);
                    } else {
                        bulletin.setDetailedImagePath("");
                    }

                    //商家(周边)编号
                    if (jsonObject.get("circumID") != null) {

                        bulletin.setBusinessID(jsonObject.get("circumID").getAsInt());
                    } else {
                        bulletin.setBusinessID(-1);
                    }

                    //发布时间
                    if (jsonObject.get("circumTime") != null && !TextUtils.isEmpty(jsonObject.get("circumTime").getAsString())) {

                        bulletin.setUpdate(jsonObject.get("circumTime").getAsString());
                    } else {
                        bulletin.setUpdate("");
                    }

                    if (i == 0) {
                        //如果是第一个item，则吧总文章数目放进去
                        bulletin.setTotalCount(totalArtclesCount);
                    }
                    objects.add(bulletin);
                }
                return objects;
            }
        }
        return null;
    }


    /**
     * 解析预约商家详情图片列表的Json数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 商家详情图片的列表。如果解析失败或文章数量为0，返回NULL，否则在[0]处返回总数.
     */
    public static ArrayList<BusinessImageBean> getBusinessImageJsonArray(String jsonString) {
        final String TAG = "Decoder_BusinessImageJsonArray";

        //将获取到的字符串转换为Json数据

        JsonElement jsonElement;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, " JSON data is invaild");
            return null;
        }

        if (jsonElement != null) {

            //总文章数量
            int totalArtclesCount = 0;
            if (jsonElement.getAsJsonObject().get("Total") != null) {
                totalArtclesCount = jsonElement.getAsJsonObject().get("Total").getAsInt();
            }

            Log.d(TAG, "requested count:" + jsonArray.size());
            Log.d(TAG, "total count:" + totalArtclesCount);

            //如果文章数量大于 0
            if (jsonArray.size() > 0) {

                //创建存储的数组
                ArrayList<BusinessImageBean> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    BusinessImageBean imageBean = new BusinessImageBean();

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();



                    //列表的标题
                    if (jsonObject.get("RelativePath") != null && !TextUtils.isEmpty(jsonObject.get("RelativePath").getAsString())) {

                        if (!TextUtils.isEmpty(jsonObject.get("RelativePath").getAsString())) {
                            String string = "";
                            string = jsonObject.get("RelativePath").getAsString();
                            string = string.replaceFirst("\\\\", "/");
                            string = NetworkHelper.BASE_URL + string;
                            imageBean.setImagePath(string);
                        }

                    } else {
                            imageBean.setImagePath("");
                    }

                    if (i == 0) {
                        //如果是第一个item，则吧总文章数目放进去
                        imageBean.setTotal(totalArtclesCount);
                    }
                    objects.add(imageBean);


                }

                return objects;
            }
        }
        return null;
    }


    /**
     * 解析预约历史列表的Json数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 商家的列表。如果解析失败或文章数量为0，返回NULL
     */
    public static ArrayList<BulletinBean> getOrderJsonArray(String jsonString) {
        final String TAG = "Decoder_OrderJsonArray";

        //将获取到的字符串转换为Json数据

        JsonElement jsonElement;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, " JSON data is invaild");
            return null;
        }

        if (jsonElement != null) {

            //总文章数量
            int totalArtclesCount = 0;
            if (jsonElement.getAsJsonObject().get("Total") != null) {
                totalArtclesCount = jsonElement.getAsJsonObject().get("Total").getAsInt();
            }

            Log.d(TAG, "requested count:" + jsonArray.size());
            Log.d(TAG, "total count:" + totalArtclesCount);

            //如果文章数量大于 0
            if (jsonArray.size() > 0) {

                //创建存储的数组
                ArrayList<BulletinBean> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    //遍历数组，生成文章的Bean对象

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    BulletinBean bulletin = new BulletinBean();

                    //列表的标题
                    if (jsonObject.get("storeName") != null && !TextUtils.isEmpty(jsonObject.get("storeName").getAsString())) {
                        bulletin.setTitle(jsonObject.get("storeName").getAsString());
                    } else {
                        bulletin.setTitle("");
                    }

                    //商家的介绍
                    if (jsonObject.get("userMsg") != null && !TextUtils.isEmpty(jsonObject.get("userMsg").getAsString())) {

                        bulletin.setIntroduce(jsonObject.get("userMsg").getAsString());
                    } else {
                        bulletin.setIntroduce("");
                    }

                    //商家的积分
                    if (jsonObject.get("rate") != null && !TextUtils.isEmpty(jsonObject.get("rate").getAsString())) {

                        bulletin.setScore(jsonObject.get("rate").getAsString());
                    } else {
                        bulletin.setScore("");
                    }


                    //商家(周边)编号
                    if (jsonObject.get("storeID") != null) {

                        bulletin.setBusinessID(jsonObject.get("storeID").getAsInt());
                    } else {
                        bulletin.setBusinessID(-1);
                    }
                    //预约ID
                    if (jsonObject.get("ID") != null) {

                        bulletin.setOrderID(jsonObject.get("ID").getAsInt());
                    } else {
                        bulletin.setOrderID(-1);
                    }
                    //预约状态
                    if (jsonObject.get("orderStatus") != null) {

                        bulletin.setOrderStatus(jsonObject.get("orderStatus").getAsInt());
                    } else {
                        bulletin.setOrderStatus(-1);
                    }
                    //预约的时间
                    if (jsonObject.get("orderTime") != null && !TextUtils.isEmpty(jsonObject.get("storeName").getAsString())) {
                        bulletin.setUpdate(jsonObject.get("orderTime").getAsString());
                    } else {
                        bulletin.setUpdate("");
                    }
                    //预约的时间
                    if (jsonObject.get("responMsg") != null && !TextUtils.isEmpty(jsonObject.get("responMsg").getAsString())) {
                        bulletin.setMassage(jsonObject.get("responMsg").getAsString());
                    } else {
                        bulletin.setMassage("");
                    }
                    //商家联系电话
                    if (jsonObject.get("userTel") != null && !TextUtils.isEmpty(jsonObject.get("userTel").getAsString())) {
                        bulletin.setPhoneNum(jsonObject.get("userTel").getAsString());
                    } else {
                        bulletin.setPhoneNum("");
                    }


                    if (i == 0) {
                        //如果是第一个item，则吧总文章数目放进去
                        bulletin.setTotalCount(totalArtclesCount);
                    }
                    objects.add(bulletin);
                }
                return objects;
            }
        }
        return null;
    }


    /**
     * 解析商家预设评论列表的Json数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 商家预设评论的列表。如果解析失败或文章数量为0，返回NULL，否则在[0]处返回总数.
     */
    public static ArrayList<ReplyTabBean> getReplyTabJsonArray(String jsonString) {
        final String TAG = "Decoder_ReplyTabJsonArray";

        //将获取到的字符串转换为Json数据

        JsonElement jsonElement;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("preinstaArray").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, " JSON data is invaild");
            return null;
        }

        if (jsonElement != null) {

            //总文章数量
            int totalArtclesCount = 0;
            if (jsonElement.getAsJsonObject().get("rowCount") != null) {
                totalArtclesCount = jsonElement.getAsJsonObject().get("rowCount").getAsInt();
            }

            Log.d(TAG, "requested count:" + jsonArray.size());
            Log.d(TAG, "total count:" + totalArtclesCount);

            //如果文章数量大于 0
            if (jsonArray.size() > 0) {

                //创建存储的数组
                ArrayList<ReplyTabBean> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    ReplyTabBean replyTabBean = new ReplyTabBean();

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();



                    //预设回复的内容
                    if (jsonObject.get("preinstallContent") != null && !TextUtils.isEmpty(jsonObject.get("preinstallContent").getAsString())) {
                        replyTabBean.setContent(jsonObject.get("preinstallContent").getAsString());
                    } else {
                        replyTabBean.setContent("");
                    }
                    //预设评价数
                    if (jsonObject.get("replyCount") != null) {

                        replyTabBean.setCount(jsonObject.get("replyCount").getAsInt());
                    } else {
                        replyTabBean.setCount(-1);
                    }



                    if (i == 0) {
                        //如果是第一个item，则吧总文章数目放进去
                        replyTabBean.setTotal(totalArtclesCount);
                    }
                    objects.add(replyTabBean);


                }

                return objects;
            }
        }
        return null;
    }


    /**
     * 解析评论列表的Json数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 商家的列表。如果解析失败或文章数量为0，返回NULL
     */
    public static ArrayList<BulletinBean> getReplyListJsonArray(String jsonString) {
        final String TAG = "Decoder_ReplyListJsonArray";

        //将获取到的字符串转换为Json数据

        JsonElement jsonElement;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, " JSON data is invaild");
            return null;
        }

        if (jsonElement != null) {

            //总文章数量
            int totalArtclesCount = 0;
            if (jsonElement.getAsJsonObject().get("Total") != null) {
                totalArtclesCount = jsonElement.getAsJsonObject().get("Total").getAsInt();
            }

            Log.d(TAG, "requested count:" + jsonArray.size());
            Log.d(TAG, "total count:" + totalArtclesCount);

            //如果文章数量大于 0
            if (jsonArray.size() > 0) {

                //创建存储的数组
                ArrayList<BulletinBean> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    //遍历数组，生成文章的Bean对象

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    BulletinBean bulletin = new BulletinBean();

                    //用户的名字
                    if (jsonObject.get("name") != null && !TextUtils.isEmpty(jsonObject.get("name").getAsString())) {
                        bulletin.setTitle(jsonObject.get("name").getAsString());
                    } else {
                        bulletin.setTitle("");
                    }

                    //用户的回复
                    if (jsonObject.get("replyContent") != null && !TextUtils.isEmpty(jsonObject.get("replyContent").getAsString())) {

                        bulletin.setIntroduce(jsonObject.get("replyContent").getAsString());
                    } else {
                        bulletin.setIntroduce("");
                    }

                    //用户的评分
                    if (jsonObject.get("replyScore") != null && !TextUtils.isEmpty(jsonObject.get("replyScore").getAsString())) {

                        bulletin.setScore(jsonObject.get("replyScore").getAsString());
                    } else {
                        bulletin.setScore("");
                    }

                    //用户发布的时间
                    if (jsonObject.get("replyTime") != null && !TextUtils.isEmpty(jsonObject.get("replyTime").getAsString())) {

                        bulletin.setUpdate(jsonObject.get("replyTime").getAsString());
                    } else {
                        bulletin.setUpdate("");
                    }
                    //评论id
                    if (jsonObject.get("ID") != null) {

                        bulletin.setCommentID(jsonObject.get("ID").getAsInt());
                    } else {
                        bulletin.setCommentID(-1);
                    }






                    if (i == 0) {
                        //如果是第一个item，则吧总文章数目放进去
                        bulletin.setTotalCount(totalArtclesCount);
                    }
                    objects.add(bulletin);
                }
                return objects;
            }
        }
        return null;
    }




    /**
     * 解析预约返回的消息
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据对象   返回的消息对象
     */
    public static OrderBean upOrderData(String jsonString) {
        final String TAG = "Decoder_upOrderData";

        JsonElement jsonElement;
        JsonObject jsonObject;



        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        OrderBean orderBean= new OrderBean();

        if (isVaild(jsonObject,"isSuccessed")) {
            orderBean.setSuccessed(jsonObject.get("isSuccessed").getAsBoolean());
        } else {
            orderBean.setSuccessed(false);
        }

        if (isVaild(jsonObject,"message")) {
            orderBean.setMessage(jsonObject.get("message").getAsString());
        } else {
            orderBean.setMessage("");
        }




        return orderBean;

    }



    /**
     * 解析提交预设评论返回的消息
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据对象   返回的消息对象
     */
    public static OrderBean upReplyTabData(String jsonString) {
        final String TAG = "Decoder_upReplyTabData";

        JsonElement jsonElement;
        JsonObject jsonObject;



        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        OrderBean orderBean= new OrderBean();

        if (isVaild(jsonObject,"isSuccessed")) {
            orderBean.setSuccessed(jsonObject.get("isSuccessed").getAsBoolean());
        } else {
            orderBean.setSuccessed(false);
        }

        if (isVaild(jsonObject,"message")) {
            orderBean.setMessage(jsonObject.get("message").getAsString());
        } else {
            orderBean.setMessage("");
        }




        return orderBean;

    }




    /**
     * 解析提交详细评论返回的消息
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据对象   返回的消息对象
     */
    public static OrderBean upReplyContentData(String jsonString) {
        final String TAG = "Decoder_upRepleyContentData";

        JsonElement jsonElement;
        JsonObject jsonObject;



        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        OrderBean orderBean= new OrderBean();

        if (isVaild(jsonObject,"isSuccessed")) {
            orderBean.setSuccessed(jsonObject.get("isSuccessed").getAsBoolean());
        } else {
            orderBean.setSuccessed(false);
        }

        if (jsonObject.get("commentID")!=null) {
            orderBean.setCommentID(jsonObject.get("commentID").getAsInt());
        } else {
            orderBean.setCommentID(-1);
        }




        return orderBean;

    }



    /**
     * 解析评论列表图片列表的Json数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 商家详情图片的列表。如果解析失败或文章数量为0，返回NULL，否则在[0]处返回总数.
     */
    public static ArrayList<BusinessImageBean> getReplyImageJsonArray(String jsonString) {
        final String TAG = "Decoder_ReplyImageJsonArray";

        //将获取到的字符串转换为Json数据

        JsonElement jsonElement;
        JsonArray jsonArray;

        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonArray = jsonElement.getAsJsonObject().get("Rows").getAsJsonArray();

        } catch (Exception e) {
            Log.e(TAG, " JSON data is invaild");
            return null;
        }

        if (jsonElement != null) {

            //总文章数量


            //如果文章数量大于 0
            if (jsonArray.size() > 0) {

                //创建存储的数组
                ArrayList<BusinessImageBean> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    BusinessImageBean imageBean = new BusinessImageBean();

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();



                    //列表的标题
                    if (jsonObject.get("RelativePath") != null && !TextUtils.isEmpty(jsonObject.get("RelativePath").getAsString())) {

                        if (!TextUtils.isEmpty(jsonObject.get("RelativePath").getAsString())) {
                            String string = "";
                            string = jsonObject.get("RelativePath").getAsString();
                            string = string.replaceFirst("\\\\", "/");
                            string = NetworkHelper.BASE_URL + string;
                            imageBean.setImagePath(string);
                        }

                    } else {
                        imageBean.setImagePath("");
                    }


                    objects.add(imageBean);


                }

                return objects;
            }
        }
        return null;
    }




    /**
     * 解析取消预约返回的消息
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据对象   返回的消息对象
     */
    public static boolean cancalOrderData(String jsonString) {
        final String TAG = "Decoder_cancalOrderData";

        JsonElement jsonElement;
        JsonObject jsonObject;



        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return false;
        }



        if (isVaild(jsonObject,"isSuccessed")) {
           return jsonObject.get("isSuccessed").getAsBoolean();
        } else {
            return  false;
        }








    }

    /**
     * 解析积分抵消数据
     *
     * @param jsonString Get到的Json数据文本
     * @return 数据数组  data[0]需消耗积分 data[1]抵消后需缴费金额
     */
    public static String[] getScoreDeductData(String jsonString) {
        final String TAG = "Decoder_GetScoreDeductData";

        JsonElement jsonElement;
        JsonObject jsonObject;



        try {

            jsonElement = new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();

        } catch (Exception e) {
            Log.e(TAG, "User JSON data is invaild");
            return null;
        }

        String [] data=new String[2];

        if (isVaild(jsonObject,"useScore")) {
            data[0]=jsonObject.get("useScore").getAsString();
        } else {
            data[0]=null;
        }
        if(isVaild(jsonObject,"restMoney")){
            data[1]=jsonObject.get("restMoney").getAsString();
        }else {
            data[1]=null;
        }




        return data;

    }


    /**
     * 判断指定JsonObject内的指定字段是否有效   包括String值为空也算无效
     *
     * @param object 要检查的JsonObject
     * @param key    要检查的字段
     * @return 是否有效
     */
    private static boolean isVaild(JsonObject object, String key) {
        return object != null && !TextUtils.isEmpty(key) && object.has(key) && !TextUtils.isEmpty(object.get(key).getAsString());
    }

}
