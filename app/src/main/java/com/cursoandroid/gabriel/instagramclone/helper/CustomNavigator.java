package com.cursoandroid.gabriel.instagramclone.helper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

@Navigator.Name("custom_fragment")
public final class CustomNavigator extends FragmentNavigator {

    private final Context context;
    private final FragmentManager manager;
    private final int containerId;

    public CustomNavigator(@NotNull Context context, @NotNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        this.context = context;
        this.manager = manager;
        this.containerId = containerId;
    }

    @org.jetbrains.annotations.Nullable
    public NavDestination navigate(@NotNull Destination destination, @org.jetbrains.annotations.Nullable Bundle args, @org.jetbrains.annotations.Nullable NavOptions navOptions, @Nullable Extras navigatorExtras) {
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        String tag = String.valueOf(destination.getId());
        FragmentTransaction var10000 = this.manager.beginTransaction();
        Intrinsics.checkExpressionValueIsNotNull(var10000, "manager.beginTransaction()");
        FragmentTransaction transaction = var10000;
        boolean initialNavigate = false;
        Fragment currentFragment = this.manager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            Intrinsics.checkExpressionValueIsNotNull(transaction.detach(currentFragment), "transaction.detach(currentFragment)");
        } else {
            initialNavigate = true;
        }

        Fragment fragment = this.manager.findFragmentByTag(tag);
        if (fragment == null) {
            String var11 = destination.getClassName();
            Intrinsics.checkExpressionValueIsNotNull(var11, "destination.className");
            String className = var11;
            fragment = this.manager.getFragmentFactory().instantiate(this.context.getClassLoader(), className);
            transaction.add(this.containerId, fragment, tag);
        } else {
            transaction.attach(fragment);
        }

        transaction.setPrimaryNavigationFragment(fragment);
        transaction.setReorderingAllowed(true);
        transaction.commitNow();
        return initialNavigate ? (NavDestination)destination : null;
    }

    // $FF: synthetic method
    // $FF: bridge method
    public NavDestination navigate(NavDestination var1, Bundle var2, NavOptions var3, Extras var4) {
        return this.navigate((Destination)var1, var2, var3, var4);
    }
}
