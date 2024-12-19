package pl.glownia.maciej.checkoutcomponent.dto;

import java.util.Objects;

public class ScanItemResponse {
    private boolean success;

    public ScanItemResponse(boolean success) {
        this.success = success;
    }

    public static ScanItemResponse success() {
        return new ScanItemResponse(true);
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScanItemResponse that = (ScanItemResponse) o;
        return success == that.success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(success);
    }

}