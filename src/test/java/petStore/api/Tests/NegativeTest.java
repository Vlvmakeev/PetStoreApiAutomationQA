package petStore.api.Tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import petStore.api.Models.MessagePetResponse;
import petStore.api.Models.PetInStoreRoot;
import petStore.api.Models.UploadImageResponse;
import petStore.api.Models.petinstoremodels.CategoryForRoot;
import petStore.api.Models.petinstoremodels.TagForRoot;

import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static petStore.config.Constants.*;

public class NegativeTest {

    private int freePetId;
    // Add new pet in store
    private String availableCategoryName = AVAILABLE_CATEGORY_NAME;
    private String availablePetName = AVAILABLE_PET_NAME;
    private String availablePhotoUrl = AVAILABLE_PHOTO_URL;
    private String availableTagName = AVAILABLE_TAG_NAME;
    private String availableStatus = AVAILABLE_STATUS_VALUE;

    // Update created pet
    private String updAvailableCategoryName = UPD_AVAILABLE_CATEGORY_NAME;
    private String updAvailablePetName = UPD_AVAILABLE_PET_NAME;
    private String updAvailablePhotoUrl = UPD_AVAILABLE_PHOTO_URL;
    private String updAvailableTagName = UPD_AVAILABLE_TAG_NAME;
    private String updAvailableStatus = UPD_AVAILABLE_STATUS_VALUE;

    @Epic(value = "Негативные тесты")
    @Story(value = "Поиск свободного идентификатора для объекта питомца")
    @Test
    @Description(value = "Тест проверяет работу метода GET /pet/{petId} и находит свободный идентификатор питомца")
    public void findFreePetId() {

        int i = 1;
        int statusCode;


        do {
            statusCode = given()
                    .when()
                    .get(BASE_URL + "/pet/" + i)
                    .then().log().all().extract().statusCode();

            if (statusCode == 404) {
                break;
            }
            i++;
        } while (statusCode == 404);

        System.out.println(i);
        this.freePetId = i;
    }

    @Epic(value = "Негативные тесты")
    @Story(value = "Добавление нового объекта питомца c пустым телом запроса")
    @Test (dependsOnMethods = {"findFreePetId"})
    @Description(value = "Тест проверяет работу метода POST /pet с пустым телом запроса и ожиданием 405-го статус-кода")
    public void addNewPetToTheStoreWithEmptyBody() {


        ArrayList<TagForRoot> tags = new ArrayList<TagForRoot>();
        tags.add(new TagForRoot(freePetId, availableTagName));
        PetInStoreRoot response = given()
                .header("content-type", "application/json")
                .body(
                        new PetInStoreRoot()
                )
                .when()
                .post(BASE_URL + "/pet")
                .then().log().all()
                .statusCode(405)
                .extract().as(PetInStoreRoot.class);
    }

    @Epic(value = "Негативные тесты")
    @Story(value = "Добавление нового изображения для добавленного объекта питомца без указания формата содержимого")
    @Test (dependsOnMethods = {"findFreePetId"})
    @Description(value = "Тест проверяет работу метода POST /pet/{petId}/uploadImage без указания формата содержимого запроса и ожиданием 410-го статус-кода")
    public void uploadImageForPetWithoutMultiparts() {
        UploadImageResponse response = given()
                .header("Content-Type", "multipart/form-data")
                .header("accept", "application/json")
                .when()
                .post(BASE_URL + "/pet/" + freePetId +"/uploadImage")
                .then().log().all()
                .statusCode(415)
                .extract().as(UploadImageResponse.class);

        Assert.assertEquals(response.getCode(), 415);
    }

    @Epic(value = "Негативные тесты")
    @Story(value = "Получение добавленного питомца по буквенному идентификатору")
    @Test (dependsOnMethods = {"findFreePetId"})
    @Description(value = "Тест проверяет работу метода GET /pet/{petId} со значением буквенных символов в поле идентификатора и ожиданием 400-го статус-кода")
    public void findPetByIdInString() {
        MessagePetResponse response = given()
                .when()
                .get(BASE_URL + "/pet/" + "string")
                .then().log().all()
                .statusCode(400)
                .extract().as(MessagePetResponse.class);

        Assert.assertEquals(response.getCode(), 400);
        Assert.assertEquals(response.getMessage(), "java.lang.NumberFormatException: For input string: \"string\"");
    }

    @Epic(value = "Негативные тесты")
    @Story(value = "Изменение добавленного объекта питомца с пустым телом ответа")
    @Test (dependsOnMethods = {"findFreePetId"})
    @Description(value = "Тест проверяет работу метода PUT /pet с пустым телом запроса и ожиданием 415-го статус-кода")
    public void updatePetWithEmptyBody() {

        ArrayList<String> updPhotoUrls = new ArrayList<>();
        updPhotoUrls.add(updAvailablePhotoUrl);

        ArrayList<TagForRoot> updTags = new ArrayList<TagForRoot>();
        updTags.add(new TagForRoot(freePetId, updAvailableTagName));

        PetInStoreRoot response = given()
                .header("content-type", "application/json")
                .when()
                .put(BASE_URL + "/pet")
                .then().log().all()
                .statusCode(415)
                .extract().as(PetInStoreRoot.class);
    }

    @Epic(value = "Негативные тесты")
    @Story(value = "Получение измененного питомца по идентификатору без указания API-ключа запроса")
    @Test (dependsOnMethods = {"findFreePetId"})
    @Description(value = "Тест проверяет работу метода DELETE /pet/{petId} без указания API-ключа запроса и ожиданием 400-го статус-кода")
    public void deletePetWithoutApiKey() {
        given()
                .header("accept", "application/json")
                .when()
                .delete(BASE_URL + "/pet/" + freePetId)
                .then().log().all()
                .statusCode(400)
                .extract().as(MessagePetResponse.class);
    }

    @Epic(value = "Негативные тесты")
    @Story(value = "Получение измененного питомца по идентификатору без указания идентификатора в параметре пути")
    @Test (dependsOnMethods = {"findFreePetId"})
    @Description(value = "Тест проверяет работу метода DELETE /pet/{petId} без указания идентификатора в параметре пути и ожиданием 400-го статус-кода")
    public void deletePetWithoutId() {
        given()
                .header("accept", "application/json")
                .header("api_key", "special-key")
                .when()
                .delete(BASE_URL + "/pet/")
                .then().log().all()
                .statusCode(400)
                .extract().as(MessagePetResponse.class);
    }

}
