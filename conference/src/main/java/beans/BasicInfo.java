package beans;

/**
 * Created by NewObject on 2017/8/13.
 */
public class BasicInfo {
    private Integer basic_id;
    private String name;
    private String gender;
    private Integer age;
    private String address;
    private String email;
    private String phone;
    private String school;
    private String introduce;

    public BasicInfo(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.school = "惠州学院";
    }

    public BasicInfo(String name) {
        this.name = name;
        this.gender = "男";
        this.school = "惠州学院";
    }

    public BasicInfo(Integer basic_id) {
        this.basic_id = basic_id;
        this.gender = "男";
        this.school = "惠州学院";
    }

    public BasicInfo() {
        this.basic_id = -1;
        this.school = "惠州学院";
        this.gender = "男";
    }

    public BasicInfo(String name, String gender, Integer age,
                     String phone, String school) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.school = school;
    }

    public BasicInfo(Integer basic_id, String name, String gender,
                     Integer age, String address, String email,
                     String phone, String school) {
        this.basic_id = basic_id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.school = school;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("basic_id:\t").append(this.basic_id)
                .append("\nname:\t").append(this.name)
                .append("\ngender:\t").append(this.gender)
                .append("\nschool:\t").append(this.school)
                .append("\nage:\t").append(this.age)
                .append("\nphone:\t").append(this.phone)
                .append("\n");
        return builder.toString();
    }

    public Integer getBasic_id() {
        return basic_id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getSchool() {
        return school;
    }

    public String getIntroduce() {
        return introduce;
    }


    public void setBasic_id(Integer basic_id) {
        this.basic_id = basic_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
