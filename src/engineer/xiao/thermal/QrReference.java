package engineer.xiao.thermal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QrReference {
    private String qrId;
    private JsonElement response = null;

    QrReference(String qrId) {
        this.qrId = qrId;
    }

    JsonObject getResponse() {
        responseNotNull();
        if(response.isJsonObject()){
            return response.getAsJsonObject();
        }
        return null;
    }

    JsonObject forceResponse() {
        updateResponse();
        if(response.isJsonObject()){
            return response.getAsJsonObject();
        }
        return null;
    }

    JsonObject getQr() {
        responseNotNull();
        JsonObject response = getResponse();
        JsonElement qr;
        if(response.has("qr")){
            qr = response.get("qr");
        } else {
            return null;
        }
        if(qr.isJsonObject()){
            return qr.getAsJsonObject();
        }
        return null;
    }

    JsonObject getSo() {
        responseNotNull();
        JsonObject response = getResponse();
        JsonElement so;
        if(response.has("so")){
            so = response.get("so");
        } else {
            return null;
        }
        if(so.isJsonObject()){
            return so.getAsJsonObject();
        }
        return null;
    }

    JsonObject getPo() {
        responseNotNull();
        JsonObject response = getResponse();
        JsonElement po;
        if(response.has("po")){
            po = response.get("po");
        } else {
            return null;
        }
        if(po.isJsonObject()){
            return po.getAsJsonObject();
        }
        return null;
    }

    JsonObject getWr() {
        responseNotNull();
        JsonObject response = getResponse();
        JsonElement wrs;
        if(response.has("wr")){
            wrs = response.get("wr");
        } else {
            return null;
        }
        if(wrs.isJsonArray()){
            JsonArray wrArr = wrs.getAsJsonArray();
            JsonElement first = wrArr.get(0);
            return first.getAsJsonObject();
        }
        return null;
    }


    private void responseNotNull() {
        if (response == null) {
            updateResponse();
        }
    }

    private void updateResponse() {
        response = XHR.get(XHR.apiServer
                + "printThermalReceipt/" + qrId);
    }

    public String getQrId() {
        return qrId;
    }

    public ArrayList<String> linesToPrint(){
        try {
            responseNotNull();
            ArrayList<String> lines = new ArrayList<>();

            JsonObject qrInfo = getQr();
            lines.add("交易流水码： "+qrId);

            JsonObject wr = getWr();
//            lines.add(
//                    "称重计量单号： "+wr.get("id").getAsString()
//            );
            lines.add(
                    "车牌号： "+wr.get("licensePlate").getAsString()
            );
//            lines.add(
//                    "驾驶员： "+wr.get("driver").getAsString()
//            );

            if(qrInfo.get("eventType").getAsString().equals("p")){
//                JsonObject po = getPo();
//                lines.add("开单时间： "+po.get("createTime").getAsString());
//                lines.add("入库单号： "+po.get("id").getAsString());
//                lines.add("供应商： "+po.get("supplier").getAsString());
//                lines.add("原材料： "+po.get("rawMaterial").getAsString());
                return lines;
            } else if(qrInfo.get("eventType").getAsString().equals("s")) {
//                JsonObject so = getSo();
//                lines.add("开单时间： "+so.get("createTime").getAsString());
//                lines.add("发货单号： "+so.get("id").getAsString());
//                lines.add("客户： "+so.get("clientName").getAsString());
//                lines.add("水泥品种： "+so.get("cementType").getAsString());
//
//                double ton = (so.get("reconciliationKG").getAsDouble()/1000);
//                DecimalFormat df = new DecimalFormat("##.00");
//                lines.add("吨位： "+df.format(ton));
                return lines;
            }
            return null;
        } catch (NullPointerException e){
            System.out.println("流水信息获取失败");
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
