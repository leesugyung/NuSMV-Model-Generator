## NuSMV Model Generator

[[src state, 'condition: event', dst state], ...]] 의 model txt 파일을 읽어
NuSMV model로 변환해주는 자동화 도구입니다.

## Menual

![image](https://user-images.githubusercontent.com/74905621/212845845-0bc39a2a-3262-4f60-bcac-5f441be922c2.png)

Command Option
- `-filePath`: 입력할 model의 파일 경로를 전달한다. <반드시 전달해야 한다.>
- `enumFilePath`: 열거형 변수에 대한 정보가 필요하면, 파일 경로를 전달한다. (default = “”)
- `moduleName`: module의 이름을 전달한다. (default = “main”)
