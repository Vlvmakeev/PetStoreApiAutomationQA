package petStore.api.Models;

public class MessagePetResponse {
    // Класс объекта тела ответа от сервера по запросам POST pet/{petId}/uploadImage, GET/pet/{petId} при 404-м статус-коде, DELETE/pet/{petId}
    public int code;
    public String type;
    public String message;

    public MessagePetResponse() {
    }

    public MessagePetResponse(int code, String type, String message) {
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
