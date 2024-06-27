## Employee Data Analysis API
### Persiapan
Pada aplikasi ini, saya menggunakan elasticsearch versi 8.13.4 dengan alasan kompabilitas dengan Spring Boot. 
sebelum dapat melakukan bulk index ke file json yang disediakan, kita harus menghapus `_type:"employee"`
terlebih dahulu karena menimbulkan error di versi Elasticsearch yang digunakan. Proses konversi dapat dilakukan dengan
[script berikut](./script_ubah_ke_versi_baru.py), dengan meletakkan file Employee50k.json pada directory yang sama dengan script.



Untuk mendownload dan mengerun Database Elasticsearch dapat 
 menggunakan Docker dengan mengeksekusi perintah:

```bash
$ docker network create elastic
$ docker pull elasticsearch:8.13.4
$ docker run --name es01 --net elastic -p 9200:9200 -it -m 1GB -e "discovery.type=single-node"  -e "xpack.security.enabled=false" elasticsearch:8.13.4
```

Setelah itu buat mapping berikut untuk index companydatabase, yang juga menambahkan field dengan tipe data keyword untuk field Designation, MaritalStatus, dan Interests
```bash
curl -X PUT "localhost:9200/companydatabase" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "FirstName": { "type": "text" },
      "LastName": { "type": "text" },
      "Designation": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "keyword"
          }
        }
      },
      "Salary": { "type": "integer" },
      "DateOfJoining": {
        "type": "date",
        "format": "yyyy-MM-dd"
      },
      "Address": { "type": "text" },
      "Gender": { "type": "text" },
      "Age": { "type": "integer" },
      "MaritalStatus": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "keyword"
          }
        }
      },
      "Interests": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "keyword"
          }
        }
      }
    }
  }
}'

```


### Cara menjalankan
Download dependensi yang ada pada file pom.xml, dan jalankan aplikasi Spring Boot

Untuk tiap endpoint yang dapat diakses dijabarkan pada file [endpoints.http](./endpoints.http)