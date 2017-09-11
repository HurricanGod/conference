package testbeans;

/**
 * Created by NewObject on 2017/8/16.
 */
public class Mark {
    private Integer id;
    private Course course;
    private Double score;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.course.toString()).append("score:\t")
                .append(this.score).append("\n");
        return builder.toString();
    }

    public Mark(Course course, Double score) {
        this.course = course;
        this.score = score;
    }

    public Mark() {
        this.id = 0;
    }

    public Course getCourse() {
        return course;
    }

    public Double getScore() {
        return score;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
