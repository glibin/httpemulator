package ru.hh.standalone.utils.oauth.fb_social_entity;

/**
 * Created by marefyev on 4/7/14.
 */
public class FbData {

    private static long lastTimeStamp = System.currentTimeMillis();
    private String firstName;
    private String lastName;
    private String gender;
    private String uid;

    public FbData(String _uid) {
        this.firstName = "Fn" + getUnique().toLowerCase();
        this.lastName = "Ln" + getUnique().toLowerCase();
        this.gender = "Мужской";
        this.uid = _uid;
    }

    public String getfbUID() {
        return this.uid;
    }

    public FbData setLastName(String _str) {
        this.lastName = _str;
        return this;
    }

    public FbData setFirstName(String _str) {
        this.firstName = _str;
        return this;
    }

    public String toString() {
        //return "{\"id\":\"" + uid +
//                "\",\"first_name\":\"" + firstName +
//                "\",\"last_name\":\"" + lastName +
//                "\",\"gender\":\"" + gender +
//                "\",\"languages\":[{\"id\":\"106059522759137\",\"name\":\"English\"},{\"id\":\"176015189120060\"," +
//                "\"name\":\"NEttttttt\"}," +
//                "{\"id\":\"173398302718393\",\"name\":\"Moscow\"}," +
//                "{\"id\":\"109489299069349\",\"name\":\"Ukrainian\"}],\"birthday\":\"02/14/1985\",\"education\":" +
//                "[{\"school\":{\"id\":\"168523519861166\",\"name\":\"Daneshmand High School\"},\"type\":\"High School\"}," +
//                "{\"concentration\":[{\"id\":\"485513701528135\",\"name\":\"DDD\"},{\"id\":\"362527137154302\",\"name\":\"DDDD\"}]," +
//                "\"school\":{\"id\":\"107570055938942\",\"name\":\"Mid Sweden University\"},\"type\":\"College\"," +
//                "\"year\":{\"id\":\"120960561375312\",\"name\":\"2013\"}}],\"email\":\"" + uid +
//                "@facebook.com\"," +
//                "\"hometown\":{\"id\":\"104005829635705\",\"name\":\"Nananana\"}," +
//                "\"location\":{\"id\":\"115085015172389\",\"name\":\"Blablabla\"}," +
//                "\"work\":[{\"employer\":{\"id\":\"172456022812339\",\"name\":\"OLearysBarRestaurant\"}}]}";

        return "{\"id\":\"" + uid +
                "\",\"first_name\":\"" + firstName +
                "\",\"last_name\":\"" + lastName +
                "\",\"gender\":\"\\u043c\\u0443\\u0436\\u0441\\u043a\\u043e\\u0439\",\"birthday\":\"04\\/29\\/1983\"," +
                "\"education\":[{\"school\":{\"id\":\"121542537871927\",\"name\":\"\\u0412\\u0443\\u0432\\u043a \\u21162\"}," +
                "\"type\":\"High School\"},{\"school\":{\"id\":\"134596036555340\",\"name\":\"\\u0412\\u0413\\u0423\"},\"type\":" +
                "\"College\"}],\"email\":\"" + uid +
                "\\u0040facebook.com\",\"hometown\":{\"id\":\"113484365328971\",\"name\":\"\\u0433. " +
                "\\u0412\\u043e\\u0440\\u043e\\u043d\\u0435\\u0436, \\u0412\\u043e\\u0440\\u043e\\u043d\\u0435\\u0436\\u0441\\u043a\\u0430\\u044f \\u043e\\u0431\\u043b.\"}," +
                "\"location\":{\"id\":\"115085015172389\",\"name\":\"\\u041c\\u043e\\u0441\\u043a\\u0432\\u0430\"},\"work\":[{\"employer\":{\"id\":\"333733483551\"," +
                "\"name\":\"HeadHunter\"},\"location\":{\"id\":\"115085015172389\",\"name\":\"\\u041c\\u043e\\u0441\\u043a\\u0432\\u0430\"},\"position\":" +
                "{\"id\":\"136216723076464\",\"name\":\"Java Team Lead\"},\"start_date\":\"2011-04-01\"}]}";


    }

    public synchronized static String getUnique() {
        long currentTimeStamp = System.currentTimeMillis();
        if (lastTimeStamp < currentTimeStamp) {
            lastTimeStamp = currentTimeStamp;
        } else {
            lastTimeStamp += 1;
        }
        final String sUnique = String.valueOf(lastTimeStamp) + String.valueOf(Math.round(Math.random() * 1000));
        char[] chars = sUnique.toCharArray();

        for (int i = 0; i < sUnique.length(); i++) {
            chars[i] += 'A' - '0';
        }

        return new String(chars);
    }
}//end class
