package com.coordinadora.technicaltest.model;

public class BackupEntity {
    public int id;
    public String etiqueta1d;
    public String observacion;

    public BackupEntity(int id, String etiqueta1d, String observacion) {
        this.id = id;
        this.etiqueta1d = etiqueta1d;
        this.observacion = observacion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BackupEntity that = (BackupEntity) obj;
        return id == that.id && etiqueta1d.equals(that.etiqueta1d) && observacion.equals(that.observacion);
    }
}
