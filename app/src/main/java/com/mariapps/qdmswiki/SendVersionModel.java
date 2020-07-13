
package com.mariapps.qdmswiki;


@SuppressWarnings("unused")
public class SendVersionModel {

    private String DeviceId;
    private String EmpId;
    private String Type;
    private String Value;


    public SendVersionModel(String deviceId, String empId, String type, String value) {
        DeviceId = deviceId;
        EmpId = empId;
        Type = type;
        Value = value;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}


