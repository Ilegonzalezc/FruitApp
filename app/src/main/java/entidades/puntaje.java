package entidades;

public class puntaje {

    private String nombre;
    private Integer score;

    public puntaje (String nombre, Integer score ){
        this.nombre = nombre;
        this.score = score;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
