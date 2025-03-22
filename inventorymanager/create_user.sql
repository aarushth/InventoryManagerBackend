USE leopard_seal_db;
GO
CREATE USER serverAcc WITH PASSWORD='MtFbwu2!'
GO
GRANT CONTROL ON DATABASE::leopard_seal_db TO serverAcc;
GO