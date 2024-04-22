# Azure Blob Manager ![version](https://img.shields.io/badge/version-1.0.0-blue) ![Angular](https://img.shields.io/badge/Angular-15-red) ![SpringBoot](https://img.shields.io/badge/SpringBoot-green) 
<img src="https://skillicons.dev/icons?i=azure"/>

Azure Blob Manager is a full-stack application built with Angular and Spring Boot designed to simplify the management of Azure Blob Storage resources, allowing users to upload, update, download, and delete files effortlessly.

## Prerequisites 

Before running Azure Blob Manager, ensure you have completed the following steps:

1. **Create an Azure Storage Account**: Create an Azure Storage Account with anonymous access enabled. This allows public access to blobs via a URL.
2. **Generate a SAS (Shared Access Signature) Token**: Generate a SAS token with a long expiration date (e.g., 2099) and grant it permissions to all resource types. This token will be used by Azure Blob Manager to access your storage account.
3. **Create a Container**: Create a container within your Azure Storage Account and set the access level to "Blob (anonymous read access for blobs only)". This ensures that blobs can be accessed publicly via URL.
4. **XAMPP:** Install XAMPP, which includes Apache HTTP Server, MySQL database, and PHP. XAMPP provides an easy-to-install Apache distribution containing MariaDB, PHP, and Perl. You can download XAMPP from [here](https://www.apachefriends.org/index.html) and follow the installation instructions.
5. **Apache and MySQL Server:** Ensure that both the Apache HTTP Server and MySQL database server are running within XAMPP. These services are required for storing file metadata (such as file names and URLs) in the MySQL database after the files are successfully uploaded to the Azure cloud provider.

## Configuration 

Once you have completed the prerequisites, configure the Azure Blob Manager application by following these steps:

1. Open the `application.properties` file in the backend Spring application.
2. Add the following properties and replace placeholders with your values:
```properties
azure.blob.connectionstring=BlobEndpoint=YOUR_TOKEN_ENDPOINT
azure.blob.container=YOUR_CONTAINER_NAME
```
## File Upload Limit 

Please note that the maximum file size allowed for uploads to the Spring server is 100MB. Additionally, the maximum request size is also limited to 100MB.

## File Manipulation Services

The backend provides a set of microservices for file manipulation, including download, upload, update, and delete operations. You can leverage these services to manage your files efficiently, you can upload any type of file (image,video,audio,pdf,txt,....) .

## Getting Started 

To get started with Azure Blob Manager, follow these steps:

1. Clone this repository to your local machine.
2. Install dependencies by running `npm install`.
3. Run the application locally using `ng serve`.
4. Access the application in your browser at `http://localhost:4200`.

## Contributing 

Contributions are welcome! If you encounter any issues or have suggestions for improvements, feel free to open an issue or submit a pull request.
