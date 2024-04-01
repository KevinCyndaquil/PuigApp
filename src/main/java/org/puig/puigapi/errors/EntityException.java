package org.puig.puigapi.errors;

public class EntityException extends RuntimeException {
    public EntityException(String message, String hint) {
        super("entity_error");
    }

    public static class NotSaved extends EntityException {

        public NotSaved(Object entity) {
            super("Entidad de tipo %s identificada como %s no pudo ser guardada"
                    .formatted(entity.getClass(), entity),
                    "La entidad ya está guardada y debes usar 'PUT' en su lugar");
        }
    }

    public static class NotUpdated extends EntityException {

        public NotUpdated(Object entity) {
            super("Entidad de tipo %s identificada como %s no pudo ser actualizada"
                            .formatted(entity.getClass().getSimpleName(), entity),
                    "La entidad no está guardada y debes usar 'POST' " +
                            "en su lugar o el id es el incorrecto");
        }
    }

    public static class NotDeleted extends EntityException {

        public NotDeleted(Object identifier) {
            super("Entidad identificada como %s no pudo ser eliminada"
                            .formatted(identifier),
                    "La entidad no está guardada o el id es el incorrento");
        }
    }

    public static class NotFind extends EntityException {
        public NotFind(Object identifier) {
            super("Entidad identificada como %s no pudo ser encontrada"
                            .formatted(identifier),
                    "La entidad no está guardada y debes usar 'POST' en su lugar");
        }
    }
}
