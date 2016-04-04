package com.omahagdg.dagger2demo.base;

import android.support.annotation.NonNull;

public interface ActivityResultDelegate {

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults);
}
