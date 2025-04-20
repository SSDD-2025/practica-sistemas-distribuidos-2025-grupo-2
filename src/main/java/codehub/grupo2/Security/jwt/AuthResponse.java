package codehub.grupo2.Security.jwt;

public class AuthResponse {
    private Status status;
    private String message;
    private String error;
    private String accessToken;
    private String refreshToken;

    public enum Status {
        SUCCESS, FAILURE
    }

    public AuthResponse() {
    }

    public AuthResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public AuthResponse(Status status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public AuthResponse(Status status, String message, String accessToken, String refreshToken) {
        this.status = status;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "AuthResponse [status=" + status + ", message=" + message + ", error=" + error +
               ", accessToken=" + accessToken + ", refreshToken=" + refreshToken + "]";
    }
}