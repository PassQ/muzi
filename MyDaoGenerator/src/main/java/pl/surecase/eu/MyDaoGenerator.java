package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.muzi.bean");

        initUserBean(schema);

        new DaoGenerator().generateAll(schema,"H:/muzi/app/src/main/java");
    }

    private static void initUserBean(Schema schema) {
        Entity userBean = schema.addEntity("SQLUserBean");
        userBean.addStringProperty("id").primaryKey().index();
        userBean.addStringProperty("orderNo");
        userBean.addStringProperty("roomID");
        userBean.addStringProperty("chargeTypeID");
        userBean.addStringProperty("chargeSum");
        userBean.addStringProperty("useScore");

    }

}
