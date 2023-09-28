package petStore.api.Models;

public class UploadImageResponse {
    // Класс объекта тела ответа от сервера по запросу POST /pet/{petId}/uploadImage
    public int code;
    public String type;
    public String message;

    public UploadImageResponse() {
    }

    public UploadImageResponse(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
