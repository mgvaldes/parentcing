package com.ing3nia.parentalcontrol.ui.components;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ing3nia.parentalcontrol.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Clase de utilidad general para la interface
 * 
 * @author Stefano
 * 
 */
public class UiUtils {
	/**
	 * Indica que el tipo de dialogo es de un solo boton(OK)
	 */
	public static final int DIALOG_OK = 0;
	/**
	 * Indica que el tipo de dialogo es de dos botones (SI y NO)
	 */
	public static final int DIALOG_SI_NO = 1;

	/**
	 * Invoca un dialog con los parametros especificados. Para que la clase que
	 * invoca reciba la notificacion de la respuesta del dialog, es necesario
	 * establecer un broadcast receiver. Agregando como filtro los strings que
	 * son broadcasted en los onClick de esta funcion. Es importante, registrar
	 * el broadcast receiver justo antes de levantar el dialogo, y
	 * desregistrarlo al recibir respuesta.
	 * 
	 * @param context
	 *            El contexto donde se levantará el dialog. Desde la clase
	 *            invocadora usar NombreDeLaClase.this.
	 * @param tipoDialogo
	 *            El tipo de dialogo a levantar, debe ser DIALOG_OK o
	 *            DIALOG_SI_NO
	 * @param mensaje
	 *            El texto a mostrar en el dialogo.
	 */
	public static void levantarDialog(final Context context, int tipoDialogo, String mensaje) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert_dialog);
		dialog.setCancelable(false);
		
		TextView textViewMensaje = (TextView) dialog.findViewById(R.id.text_view_texto_alerta);
		textViewMensaje.setText(mensaje);
		
		if (tipoDialogo == DIALOG_OK) {
			dialog.findViewById(R.id.contenedor_botones_si_no).setVisibility(View.GONE);
			((Button)dialog.findViewById(R.id.boton_ok)).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						Intent i = new Intent();
//						i.setAction(context.getString(R.string.dialog_intent_ok));
//						context.sendBroadcast(i);
						dialog.dismiss();
					}
				}
			);
		} 
		else if (tipoDialogo == DIALOG_SI_NO) {
			dialog.findViewById(R.id.boton_ok).setVisibility(View.GONE);
			((Button)dialog.findViewById(R.id.boton_no)).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						Intent i = new Intent();
//						i.setAction(context.getString(R.string.dialog_intent_no));
//						context.sendBroadcast(i);
						dialog.dismiss();
					}
				}
			);
			((Button) dialog.findViewById(R.id.boton_si)).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						Intent i = new Intent();
//						i.setAction(context
//								.getString(R.string.dialog_intent_si));
//						context.sendBroadcast(i);
						dialog.dismiss();
					}
				}
			);
		} 
		else {
			return;
		}
		
		dialog.show();
	}
}
