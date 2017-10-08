package com.ocwvar.muzi.Utils.PayUtils;

/**
 * Created by ${HLWU} on 2016/5/26.
 */
public class PayUtils {


   /*// 商户PID
    public static final String PARTNER = "2088022708336522";
    // 商户收款账号
    public static final String SELLER = "admin@gxmuzi.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwgg"
            + "JdAgEAAoGBAN21uMp7J7jQ0FcU/4ns0lQb9OLhqRjhLjirv/pfmphIfUq07ftCaKL1MntI"
            + "pu6JDyTs1g+QjQcapeYSR+B7bMIKZKuER4+wuqcIcv9g20ieTg8d43PaT7Yv6JsdYd2KJh"
            + "XsoDUCr6OWAc3jdkdRBitoQo0bwEPj30/FePOlZvcnAgMBAAECgYAs8xXW/2cLQlKrBRfV"
            + "98ygvtZEVWn6IFVFqhSPB2kJbNDyH4KMSWd7emjUs+McQ29tlhLELxNYmF+AKL6n1X4z7Dm"
            + "DuCfN4E/XtGDqYX+vT9O9Du+lzk8L5IyFo/Zgluddgd/WN4fRPKRRRyw+82zYUU5BgtqoyK4"
            + "w6EXo3W+QUQJBAPsA8E/Wm2007yKDQjQhjUJqVqE+sNNjz2ewnFnrkIMbsm9a22C4nxvfLB4qW"
            + "r7iQ+VfUNfXKt+yi4pxjKuQdKkCQQDiH4IQv62l7JClq1OfUvcxNlbs4Rd0pk4tJnpVGcU1lyU"
            + "jCYFVtKW2n3HdFMeamjVYzC0oac7U/Zb2rFz9Lp9PAkAe/PeuM6WOrvlS/nd+7iF9cJlvYMj94B"
            + "WsDH7RDycROKsn2kS5RTHHhB7zPnFjnZWaTNEzRIozgdx+ZBRk3fm5AkEAgrRnwDqrqy805S5FJ"
            + "eEVFZMtZ7ssjbbblWmaaLp+rd1yD/Ccm+fRqeA8NuieABWHJI7Qw311mtQCgQ9cqNRcLQJBAJBU"
            + "CR9LcC1fWnoLofVp8wWaOlMv/7/m09gFme1gv09x88+QZ3DiwglG8Pm9gpwLGbB7T2vvFWmi9HoZQlrPnAE=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";*/

    // 商户PID
    public static final String PARTNER = "2088221942042541";
    // 商户收款账号
    public static final String SELLER = "anhe@ahwyapp.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMTGRW3rgHcN2a04NJa83y08YhWPKvD5slW0/lFE9MwAjRBvJkZ4MCk5iEmrnw2atA+uePQHsZWSKLdDR1Qg7r3o0m7aDs1ZKLmcYVSxJylYF8k3nwD0UZN79iJI9NsdetH7chScovb8jEikYaPahVL9jLsPIFyfiVipDSGiwrY9AgMBAAECgYEAhc8ma4cYBt2eRtLPwCmTwlkrgemnk9LY2LuJrY4swYUmaCSNbKJSbdY3vZE9Sa0IARnuGlQZCNMpaOYRXT4/2idq7H/vTUT2ZVA3Fe2TQJrP0mTPNoPJfBuuGtC125PuR4ubNDjCRXJjINH3pv4UGrp0AmtVXg7D+xPxOSngHcECQQDkVE92AQUP8eyZyLS7p63jcehew0gw+3CEPnoOOyqsSPdHbdGH6EGsXFDJkw5ZCkuaEtVQMyN+RzQRTcoZFHyVAkEA3J8A30r/gpQWJJautN4xbEJD8ackc4OKQThZb6Ba8gRZ3vTnHXgkal3Vy9ibp1ok15NUAWaafq7q7plvTSbBCQJAXFJmyr0A0gCs/UV1JodXPSb6oQ5u6zdPuHkWmxh7WawrKBuUZFKZ81pkZAzs+T27cfH5hHMY/99Eg1ajx95LvQJBALzi7W4/2c7EVsjwsZds/MeBXIaS4aR8pGv+gKQPmI3Ip+E5sH4MlLD0sc0bIPkBNrXTD4M7g16fB8lMWHynh3ECQQC10/LJ96vPE5JYpgeZoh3J4RxumjiCn0yrWcmVHr5aJAf89PvUBdXXMQY0TYgXXFaUVtWHM0n+pPyt2y7LxRSb";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";

    public PayUtils() {

    }

    public static String getPARTNER() {
        return PARTNER;
    }

    public static String getRsaPrivate() {
        return RSA_PRIVATE;
    }

    public static String getRsaPublic() {
        return RSA_PUBLIC;
    }

    public static String getSELLER() {
        return SELLER;
    }


}
