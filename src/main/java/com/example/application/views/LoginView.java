package com.example.application.views;

import com.mysql.cj.log.Log;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Ubytovací systém")
@AnonymousAllowed
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    //private final LoginForm login = new LoginForm();

    public LoginView(){
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Ubytovací systém");
        i18n.getHeader().setDescription("Vítejte ales/password, admin/password");
        //i18n.setAdditionalInformation(null);
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Přihlášení");
        i18nForm.setUsername("Uživatelské jméno");
        i18nForm.setPassword("Heslo");
        i18nForm.setSubmit("Přihlásit se");
        i18n.setForm(i18nForm);
        setForgotPasswordButtonVisible(false);
        setOpened(true);
        setI18n(i18n);
        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Chyba");
        i18nErrorMessage.setMessage(
                "Zkontrolute, zda jste zadali správné uživatelské jméno a heslo.");
        i18n.setErrorMessage(i18nErrorMessage);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
       if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            //login.setError(true);
            setError(true);
        }
    }
}