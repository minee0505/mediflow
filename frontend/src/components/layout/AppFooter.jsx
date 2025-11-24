import React from 'react';
import styles from './AppFooter.module.scss';

const AppFooter = () => {
    return (
        <footer className={styles.footer}>
            <div className={styles.container}>
                © {new Date().getFullYear()} MediFlow. All rights reserved.
            </div>
        </footer>
    );
};

export default AppFooter;

