package it.brunorenz.mybank.mybankconfiguration.servicebean;

public abstract class BaseResponse {

    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
