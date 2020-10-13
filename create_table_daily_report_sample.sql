CREATE DATABASE daily_report_sample DEFAULT CHARACTER SET utf8;
CREATE USER 'repuser'@'localhost' IDENTIFIED BY 'reppass';
GRANT ALL PRIVILEGES ON daily_report_sample.* to 'repuser'@'localhost';