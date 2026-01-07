package Model;

public class Course {
	public String name;
	public String professor;
	public int studentCount;
	public int duration;

	public Course(String name, String prof, int stds, int dur) {
		this.name = name;
		this.professor = prof;
		this.studentCount = stds;
		this.duration = dur;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
