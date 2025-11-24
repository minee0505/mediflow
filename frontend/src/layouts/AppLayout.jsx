import React from 'react';
import { Outlet } from 'react-router-dom';
import AppHeader from '../components/layout/AppHeader';
import AppFooter from '../components/layout/AppFooter';
import styles from './AppLayout.module.scss';

const AppLayout = () => {
    return (
        <div className={styles.appLayout}>
            <AppHeader />
            <main className={styles.main}>
                <Outlet />
            </main>
            <AppFooter />
        </div>
    );
};

export default AppLayout;