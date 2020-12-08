package server.http;

public enum ParamType {
    BY_NAME {
        @Override
        public String toString() {
            return "name";
        }
    },
    BY_ID {
        @Override
        public String toString() {
            return "id";
        }
    }
}
