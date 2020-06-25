package engineer.xiao.thermal;

public class SalesOrder {
    private String qrId;
    private String soId;
    private String client;
    private String createTime;
    private String licensePlate;

    public SalesOrder(String qrId, String soId, String client, String createTime, String licensePlate) {
        this.qrId = qrId;
        this.soId = soId;
        this.client = client;
        this.createTime = createTime;
        this.licensePlate = licensePlate;
    }

    public String getQrId() {
        return qrId;
    }

    public String getSoId() {
        return soId;
    }

    public String getClient() {
        return client;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}
