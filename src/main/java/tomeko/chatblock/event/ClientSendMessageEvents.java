package tomeko.chatblock.event;

//? if 1.8.9 {
/*public final class ClientSendMessageEvents {
    private ClientSendMessageEvents() {}

    public static final Event<Allow> ALLOW = Event.create(Allow.class, listeners -> message -> {
        for (Allow listener : listeners) {
            if (!listener.allowSendChatMessage(message)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<Modify> MODIFY = Event.create(Modify.class, listeners -> message -> {
        for (Modify listener : listeners) {
            message = listener.modifySendChatMessage(message);
            if (message == null) {
                return null;
            }
        }
        return message;
    });

    public static final Event<Chat> CHAT = Event.create(Chat.class, listeners -> message -> {
        for (Chat listener : listeners) {
            listener.onSendChatMessage(message);
        }
    });

    @FunctionalInterface
    public interface Allow {
        boolean allowSendChatMessage(String message);
    }

    @FunctionalInterface
    public interface Modify {
        String modifySendChatMessage(String message);
    }

    @FunctionalInterface
    public interface Chat {
        void onSendChatMessage(String message);
    }
}
*///?}