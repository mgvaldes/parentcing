package com.ing3nia.parentalcontrol.shared;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.view.client.SelectionModel;

public class ToggleButtonActionCell<C> extends AbstractCell<C> {
	private SelectionModel<C> selectionModel;

	/**
	 * An html string representation of a pressed button.
	 */
	private static final SafeHtml TOGGLE_BUTTON_UP = SafeHtmlUtils.fromSafeConstant("<button type=\"button\" class=\"toggleButton toggleButton-up\" tabindex=\"-1\" aria-pressed=\"true\">");

	/**
	 * An html string representation of an unpressed.
	 */
	private static final SafeHtml TOGGLE_BUTTON_DOWN = SafeHtmlUtils.fromSafeConstant("<button type=\"button\" class=\"toggleButton toggleButton-down\" tabindex=\"-1\" aria-pressed=\"false\">");

	/**
	 * The delegate that will handle events from the cell.
	 * 
	 * @param <T>
	 *            the type that this delegate acts on
	 */
	public static interface Delegate<T> {
		/**
		 * Perform the desired action on the given object.
		 * 
		 * @param object
		 *            the object to be acted upon
		 */
		void execute(T object, Boolean wasPressed);
	}

	private final SafeHtml upImage;
	private final SafeHtml downImage;

	protected final SafeHtml htmlPressed;
	protected final SafeHtml htmlUnPpressed;

	private final Delegate<C> delegate;

	SafeHtmlBuilder sb;

	public ToggleButtonActionCell(Image upImage, Image downImage, SelectionModel<C> selectionModel, Delegate<C> delegate) {
		super("click");
		this.upImage = new SafeHtmlBuilder().appendHtmlConstant(upImage.toString()).toSafeHtml();
		this.downImage = new SafeHtmlBuilder().appendHtmlConstant(downImage.toString()).toSafeHtml();
		this.delegate = delegate;
		this.htmlPressed = new SafeHtmlBuilder().append(TOGGLE_BUTTON_UP).append(this.downImage).appendHtmlConstant("</button>").toSafeHtml();
		this.htmlUnPpressed = new SafeHtmlBuilder().append(TOGGLE_BUTTON_DOWN).append(this.upImage).appendHtmlConstant("</button>").toSafeHtml();
		this.selectionModel = selectionModel;
	}

	@Override
	public void render(Context context, C value, SafeHtmlBuilder sb) {
		this.sb = sb;
		
		if (selectionModel.isSelected(value)) {
			sb.append(htmlPressed);
		} 
		else {
			sb.append(htmlUnPpressed);
		}
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {

		if ("click".equals(event.getType())) {
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			onEnterKeyDown(context, parent, value, event, valueUpdater);
			selectionModel
					.setSelected(value, !selectionModel.isSelected(value));
			toggleButtonImage(parent, context, selectionModel.isSelected(value));
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
		delegate.execute(value, selectionModel.isSelected(value));
	}

	private void toggleButtonImage(Element parent, Context context, Boolean setPressed) {
		com.google.gwt.user.client.Element button = getButton(parent);

		if (setPressed) {
			setPressed(button, true);
			setButtonImage(button, downImage);
		} 
		else {
			setPressed(button, false);
			setButtonImage(button, upImage);
		}
	}

	private void setPressed(com.google.gwt.user.client.Element button, boolean isPressed) {
		DOM.setElementAttribute(button, "aria-pressed", Boolean.toString(isPressed));
		
		if (isPressed) {
			DOM.setElementAttribute(button, "class", "toggleButton toggleButton-down");
		} 
		else {
			DOM.setElementAttribute(button, "class", "toggleButton toggleButton-up");
		}
	}

	private void setButtonImage(Element button, SafeHtml image) {
		Element img = button.getFirstChildElement();
		button.removeChild(img);
		button.setInnerHTML(image.asString());
	}

	private com.google.gwt.user.client.Element getButton(Element parent) {
		com.google.gwt.user.client.Element button = parent.getFirstChildElement().cast();
		return button;
	}
}