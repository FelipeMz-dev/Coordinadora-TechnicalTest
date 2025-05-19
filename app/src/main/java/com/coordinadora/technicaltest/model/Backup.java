package com.coordinadora.technicaltest.model;

public class Backup {
    public int id;
    public String etiqueta1d;
    public double latitud;
    public double longitud;
    public String observacion;

    public Backup(
            int id,
            String etiqueta1d,
            double latitud,
            double longitud,
            String observacion
    ) {
        this.id = id;
        this.etiqueta1d = etiqueta1d;
        this.latitud = latitud;
        this.longitud = longitud;
        this.observacion = observacion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Backup that = (Backup) obj;
        return id == that.id && etiqueta1d.equals(that.etiqueta1d) && observacion.equals(that.observacion);
    }
}
