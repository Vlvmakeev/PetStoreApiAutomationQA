package petStore.api.Models;

import petStore.api.Models.petinstoremodels.CategoryForRoot;
import petStore.api.Models.petinstoremodels.TagForRoot;

import java.util.ArrayList;

public class PetInStoreRoot {
    // / Класс объекта тела запроса к серверу по методам POST /pet, GET /pet/{petId}, PUT /pet
    public int id;
    public CategoryForRoot category;
    public String name;
    public ArrayList<String> photoUrls;
    public ArrayList<TagForRoot> tags;
    public String status;

    public PetInStoreRoot() {
    }

    public PetInStoreRoot(int id, CategoryForRoot category, String name, ArrayList<String> photoUrls, ArrayList<TagForRoot> tags, String status) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.photoUrls = photoUrls;
        this.tags = tags;
        this.status = status;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CategoryForRoot getCategory() {
        return category;
    }

    public void setCategory(CategoryForRoot category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(ArrayList<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public ArrayList<TagForRoot> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagForRoot> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
