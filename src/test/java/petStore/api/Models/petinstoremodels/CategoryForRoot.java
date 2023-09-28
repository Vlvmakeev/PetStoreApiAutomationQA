package petStore.api.Models.petinstoremodels;

public class CategoryForRoot {
    // POJO-класс для поля category класса PetInStoreRoot
    public int id;
    public String name;

    public CategoryForRoot() {
    }

    public CategoryForRoot(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
