package com.example.servicefinder;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // UI: Button + Scrollable TextView
        Button btn = new Button(this);
        btn.setText("Scan installed services");

        final TextView out = new TextView(this);
        out.setTextSize(12f);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(out);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(btn);
        layout.addView(scroll);

        setContentView(layout);

        btn.setOnClickListener(v -> {
            out.setText("Scanning...\n");
            // run scan on background thread
            new Thread(() -> {
                final String report = scanServices();
                // post result to UI thread
                new Handler(Looper.getMainLooper()).post(() -> out.setText(report));
            }).start();
        });
    }

    private String scanServices() {
        PackageManager pm = getPackageManager();
        StringBuilder sb = new StringBuilder();
        sb.append("Service scan report\n");
        sb.append("===================\n\n");

        List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_SERVICES);
        int totalServices = 0;
        int flagged = 0;

        if (packages == null) {
            sb.append("No packages found or permission denied.\n");
            return sb.toString();
        }

        for (PackageInfo pkg : packages) {
            ServiceInfo[] services = pkg.services;
            if (services == null || services.length == 0) continue;

            boolean anyInPackage = false;
            for (ServiceInfo s : services) {
                totalServices++;
                boolean exported = s.exported;
                String perm = s.permission; // may be null
                String shortName = s.name;
                int idx = shortName.lastIndexOf('.');
                if (idx != -1) shortName = shortName.substring(idx + 1);
                String name = s.packageName + "/" + shortName;

                StringBuilder flags = new StringBuilder();
                if (exported) flags.append("EXPORTED");
                if (perm == null) {
                    if (flags.length() > 0) flags.append(", ");
                    flags.append("NO_PERMISSION");
                } else {
                    try {
                        PermissionInfo pinfo = pm.getPermissionInfo(perm, 0);
                        int protBase = pinfo.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE;
                        if (flags.length() > 0) flags.append(", ");
                        switch (protBase) {
                            case PermissionInfo.PROTECTION_SIGNATURE:
                                flags.append("perm:signature");
                                break;
                            case PermissionInfo.PROTECTION_DANGEROUS:
                                flags.append("perm:dangerous");
                                break;
                            case PermissionInfo.PROTECTION_NORMAL:
                                flags.append("perm:normal");
                                break;
                            default:
                                flags.append("perm:other");
                                break;
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        if (flags.length() > 0) flags.append(", ");
                        flags.append("perm:UNKNOWN");
                    }
                }

                // weak criteria:
                // exported == true AND (permission == null OR permission not signature-protected)
                boolean weak;
                if (!exported) {
                    weak = false;
                } else if (perm == null) {
                    weak = true;
                } else {
                    try {
                        PermissionInfo pinfo = pm.getPermissionInfo(perm, 0);
                        int protBase = pinfo.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE;
                        weak = (protBase != PermissionInfo.PROTECTION_SIGNATURE);
                    } catch (PackageManager.NameNotFoundException e) {
                        weak = true;
                    }
                }

                if (weak) {
                    if (!anyInPackage) {
                        sb.append("Package: ").append(pkg.packageName).append("\n");
                        anyInPackage = true;
                    }
                    flagged++;
                    sb.append("  - ").append(name).append("\n");
                    sb.append("      exported=").append(exported)
                            .append(", permission=").append(perm == null ? "NULL" : perm)
                            .append(", flags=").append(flags.toString()).append("\n");
                }
            }
        }

        sb.append("\nSummary: total services scanned: ").append(totalServices).append("\n");
        sb.append("         flagged (exported & weakly-protected): ").append(flagged).append("\n\n");
        sb.append("Notes:\n");
        sb.append(" - EXPORTED services are reachable by other apps unless protected by a signature-level permission.\n");
        sb.append(" - NO_PERMISSION means the service does not require a permission to bind; often risky.\n");
        sb.append(" - If a service requires a signature permission it may be much safer (only same-signer apps).\n");
        return sb.toString();
    }
}
