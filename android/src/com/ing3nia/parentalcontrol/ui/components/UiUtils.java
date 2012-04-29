package com.ing3nia.parentalcontrol.ui.components;

import com.ing3nia.parentalcontrol.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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
	 * Indica que el tipo de dialogo es de dos botones (EDIT y DELETE)
	 */
	public static final int DIALOG_EDIT_DELETE = 2;

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
		if (tipoDialogo == DIALOG_OK) {
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
	    	alert.setIcon(R.drawable.alert_icon);
	        alert.setTitle("Alert");
	        alert.setMessage(mensaje);
	        alert.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		dialog.dismiss();
	             }
	         });
	         
	         alert.create().show();
		}
		
//		final Dialog dialog = new Dialog(context);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////		dialog.setContentView(R.layout.custom_alert_dialog);
//		dialog.setCancelable(false);
//		
//		TextView textViewMensaje = (TextView) dialog.findViewById(R.id.text_view_texto_alerta);
//		textViewMensaje.setText(mensaje);
//		
//		if (tipoDialogo == DIALOG_OK) {
//			dialog.findViewById(R.id.contenedor_botones_si_no).setVisibility(View.GONE);
//			dialog.findViewById(R.id.contenedor_botones_edit_delete).setVisibility(View.GONE);
//			((Button)dialog.findViewById(R.id.boton_ok)).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
////						Intent i = new Intent();
////						i.setAction(context.getString(R.string.dialog_intent_ok));
////						context.sendBroadcast(i);
//						dialog.dismiss();
//					}
//				}
//			);
//		} 
//		else if (tipoDialogo == DIALOG_SI_NO) {
//			dialog.findViewById(R.id.boton_ok).setVisibility(View.GONE);
//			dialog.findViewById(R.id.contenedor_botones_edit_delete).setVisibility(View.GONE);
//			((Button)dialog.findViewById(R.id.boton_no)).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
////						Intent i = new Intent();
////						i.setAction(context.getString(R.string.dialog_intent_no));
////						context.sendBroadcast(i);
//						dialog.dismiss();
//					}
//				}
//			);
//			((Button) dialog.findViewById(R.id.boton_si)).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
////						Intent i = new Intent();
////						i.setAction(context
////								.getString(R.string.dialog_intent_si));
////						context.sendBroadcast(i);
//						dialog.dismiss();
//					}
//				}
//			);
//		}
//		else if (tipoDialogo == DIALOG_EDIT_DELETE) {
//			dialog.findViewById(R.id.boton_ok).setVisibility(View.GONE);
//			dialog.findViewById(R.id.contenedor_botones_si_no).setVisibility(View.GONE);
//			((Button)dialog.findViewById(R.id.boton_edit)).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
////						Intent i = new Intent();
////						i.setAction(context.getString(R.string.dialog_intent_no));
////						context.sendBroadcast(i);
//						dialog.dismiss();
//					}
//				}
//			);
//			((Button) dialog.findViewById(R.id.boton_delete)).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
////						Intent i = new Intent();
////						i.setAction(context
////								.getString(R.string.dialog_intent_si));
////						context.sendBroadcast(i);
//						dialog.dismiss();
//					}
//				}
//			);
//		}
//		else {
//			return;
//		}
//		
//		dialog.show();
	}
}
