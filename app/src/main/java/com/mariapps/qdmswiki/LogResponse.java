
package com.mariapps.qdmswiki;



public class LogResponse {

    private CommonEntity commonEntity;

    public CommonEntity getCommonEntity() {
        return commonEntity;
    }

    public void setCommonEntity(CommonEntity commonEntity) {
        this.commonEntity = commonEntity;
    }

    public static class CommonEntity {

        private Object apiToken;
        private String companyName;
        private String isAuthourized;
        private String message;
        private String timeStamp;
        private String transactionStatus;


        public Object getApiToken() {
            return apiToken;
        }

        public void setApiToken(Object apiToken) {
            this.apiToken = apiToken;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getIsAuthourized() {
            return isAuthourized;
        }

        public void setIsAuthourized(String isAuthourized) {
            this.isAuthourized = isAuthourized;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getTransactionStatus() {
            return transactionStatus;
        }

        public void setTransactionStatus(String transactionStatus) {
            this.transactionStatus = transactionStatus;
        }
    }

}
