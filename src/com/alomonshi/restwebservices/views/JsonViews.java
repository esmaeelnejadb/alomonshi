package com.alomonshi.restwebservices.views;

public class JsonViews {

    public static class NormalViews {}

    public static class ClientViews extends NormalViews{}

    public static class SubAdminViews extends ClientViews{}

    public static class AdminViews extends SubAdminViews{}

    public static class ManagerViews extends AdminViews{}

    public static class HiddenViews {}
}
