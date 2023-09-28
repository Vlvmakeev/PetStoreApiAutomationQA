package petStore.api.Tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import petStore.api.Models.MessagePetResponse;
import petStore.api.Models.UploadImageResponse;
import petStore.api.Models.PetInStoreRoot;
import petStore.api.Models.petinstoremodels.CategoryForRoot;
import petStore.api.Models.petinstoremodels.TagForRoot;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static petStore.config.Constants.*;

public class PositiveTests {

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

    @Epic(value = "Позитивные тесты")
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

    @Epic(value = "Позитивные тесты")
    @Story(value = "Добавление нового объекта питомца")
    @Test (dependsOnMethods = {"findFreePetId"})
    @Description(value = "Тест проверяет работу метода добавления объекта нового питомца в магазин POST /pet")
    public void addNewPetToTheStore() {
        ArrayList<String> photoUrls = new ArrayList<>();
        photoUrls.add(availablePhotoUrl);

        ArrayList<TagForRoot> tags = new ArrayList<TagForRoot>();
        tags.add(new TagForRoot(freePetId, availableTagName));
        PetInStoreRoot response = given()
                .header("content-type", "application/json")
                .body(
                        new PetInStoreRoot(
                                freePetId,
                                new CategoryForRoot(freePetId, availableCategoryName),
                                availablePetName, photoUrls, tags, availableStatus)
                )
                .when()
                .post(BASE_URL + "/pet")
                .then().log().all()
                .statusCode(200)
                .extract().as(PetInStoreRoot.class);

        Assert.assertEquals(response.getId(), freePetId);

        Assert.assertEquals(response.getCategory().getId(), freePetId);
        Assert.assertEquals(response.getCategory().getName(), availableCategoryName);

        Assert.assertEquals(response.getName(), availablePetName);

        Assert.assertEquals(response.getPhotoUrls().get(0), availablePhotoUrl);

        Assert.assertEquals(response.getTags().get(0).getId(), freePetId);
        Assert.assertEquals(response.getTags().get(0).getName(), availableTagName);

        Assert.assertEquals(response.getStatus(), availableStatus);

    }

    @Epic(value = "Позитивные тесты")
    @Story(value = "Добавление нового изображения для добавленного объекта питомца")
    @Test (dependsOnMethods = {"addNewPetToTheStore"})
    @Description(value = "Тест проверяет работу метода добавления изображения питомца POST /pet/{petId}/uploadImage")
    public void uploadImageForPet() {
        UploadImageResponse response = given()
                .header("Content-Type", "multipart/form-data")
                .header("accept", "application/json")
                .multiPart(new File("src/test/resources/dog.jpg"))
                .multiPart("additionalMetadata", "image jpg")
                .when()
                .post(BASE_URL + "/pet/" + freePetId +"/uploadImage")
                .then().log().all()
                .statusCode(200)
                .extract().as(UploadImageResponse.class);

        Assert.assertEquals(response.getCode(), 200);
    }

    @Epic(value = "Позитивные тесты")
    @Story(value = "Получение добавленного питомца по идентификатору")
    @Test (dependsOnMethods = {"uploadImageForPet"})
    @Description(value = "Тест проверяет работу метода GET /pet/{petId} при существующем объекте по данному идентификатору")
    public void findPetById() {
        PetInStoreRoot response = given()
                .when()
                .get(BASE_URL + "/pet/" + freePetId)
                .then().log().all()
                .statusCode(200)
                .extract().as(PetInStoreRoot.class);

        Assert.assertEquals(response.getId(), freePetId);

        Assert.assertEquals(response.getCategory().getId(), freePetId);
        Assert.assertEquals(response.getCategory().getName(), availableCategoryName);

        Assert.assertEquals(response.getName(), availablePetName);

        Assert.assertEquals(response.getPhotoUrls().get(0), availablePhotoUrl);

        Assert.assertEquals(response.getTags().get(0).getId(), freePetId);
        Assert.assertEquals(response.getTags().get(0).getName(), availableTagName);

        Assert.assertEquals(response.getStatus(), availableStatus);
    }

    @Epic(value = "Позитивные тесты")
    @Story(value = "Изменение добавленного объекта питомца")
    @Test (dependsOnMethods = {"findPetById"})
    @Description(value = "Тест проверяет работу метода изменения объекта нового питомца PUT /pet")
    public void updatePet() {

        ArrayList<String> updPhotoUrls = new ArrayList<>();
        updPhotoUrls.add(updAvailablePhotoUrl);

        ArrayList<TagForRoot> updTags = new ArrayList<TagForRoot>();
        updTags.add(new TagForRoot(freePetId, updAvailableTagName));

        PetInStoreRoot response = given()
                .header("content-type", "application/json")
                .body(
                        new PetInStoreRoot(
                                freePetId,
                                new CategoryForRoot(
                                        freePetId,
                                        updAvailableCategoryName
                                ),
                                updAvailablePetName,
                                updPhotoUrls, updTags, updAvailableStatus))
                .when()
                .put(BASE_URL + "/pet")
                .then().log().all()
                .statusCode(200)
                .extract().as(PetInStoreRoot.class);

        Assert.assertEquals(response.getId(), freePetId);

        Assert.assertEquals(response.getCategory().getId(), freePetId);
        Assert.assertEquals(response.getCategory().getName(), updAvailableCategoryName);

        Assert.assertEquals(response.getName(), updAvailablePetName);

        Assert.assertEquals(response.getPhotoUrls().get(0), updAvailablePhotoUrl);

        Assert.assertEquals(response.getTags().get(0).getId(), freePetId);
        Assert.assertEquals(response.getTags().get(0).getName(), updAvailableTagName);

        Assert.assertEquals(response.getStatus(), updAvailableStatus);
    }

    @Epic(value = "Позитивные тесты")
    @Story(value = "Получение измененного питомца по идентификатору")
    @Test (dependsOnMethods = {"updatePet"})
    @Description(value = "Тест проверяет работу метода GET /pet/{petId} при измененном объекте по данному идентификатору")
    public void findPetByIdAfterUpdate() {
        PetInStoreRoot response = given()
                .when()
                .get(BASE_URL + "/pet/" + freePetId)
                .then().log().all()
                .statusCode(200)
                .extract().as(PetInStoreRoot.class);

        Assert.assertEquals(response.getId(), freePetId);

        Assert.assertEquals(response.getCategory().getId(), freePetId);
        Assert.assertEquals(response.getCategory().getName(), updAvailableCategoryName);

        Assert.assertEquals(response.getName(), updAvailablePetName);

        Assert.assertEquals(response.getPhotoUrls().get(0), updAvailablePhotoUrl);

        Assert.assertEquals(response.getTags().get(0).getId(), freePetId);
        Assert.assertEquals(response.getTags().get(0).getName(), updAvailableTagName);

        Assert.assertEquals(response.getStatus(), updAvailableStatus);
    }
    @Epic(value = "Позитивные тесты")
    @Story(value = "Удаление объекта питомца по идентификатору")
    @Test (dependsOnMethods = {"findPetByIdAfterUpdate"})
    @Description(value = "Тест проверяет работу метода DELETE /pet/{petId} для удаления объекта питомца из магазина")
    public void deletePet() {
        MessagePetResponse response = given()
                .header("accept", "application/json")
                .header("api_key", "special-key")
                .when()
                .delete(BASE_URL + "/pet/" + freePetId)
                .then().log().all()
                .statusCode(200)
                .extract().as(MessagePetResponse.class);

        Assert.assertEquals(response.getCode(), 200);
        Assert.assertEquals(response.message, String.valueOf(freePetId));
    }
}
