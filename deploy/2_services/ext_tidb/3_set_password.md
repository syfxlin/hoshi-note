- kubectl port-forward -n services tidb-tidb-0 4000:4000 --address 0.0.0.0
- mysql -h 127.0.0.1 -P 4000 -u root
- SET PASSWORD FOR 'root'@'%' = 'password';
- FLUSH PRIVILEGES;
- 测试: mysql -h 127.0.0.1 -P 4000 -u root -p
