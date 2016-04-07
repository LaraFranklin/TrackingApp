package com.rawals.mymapa;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ramon on 31/3/16.
 */
public class LazyAdapter extends BaseAdapter {

    private LayoutInflater inflater = null; // Objeto para expandir el XML del layout a objetos
    private List<carrera> carreras = null;  // Estructura con los datos

    public LazyAdapter(Activity activity, List<carrera> carreras) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.carreras = carreras;
    }

    // Implementación de los métodos del interfaz requeridos

    @Override
    public int getCount() {
        return carreras.size();   // Número de filas a crear
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Crear una fila
        View vi = inflater.inflate(R.layout.fila, null);

        // Obtener las referencias a los controles
        TextView fec = (TextView) vi.findViewById(R.id.fecha);
        TextView dis = (TextView) vi.findViewById(R.id.distancia);
        TextView dur = (TextView) vi.findViewById(R.id.duracion);


        // Rellenar los datos
        carrera c = carreras.get(position);
        fec.setText(c.getFecha());
        dis.setText(c.getDistancia());
        dur.setText(c.getDuracion());

        // Devolver la fila generada y rellenada
        return vi;
    }

}
