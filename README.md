# KompresjaDicom
Repozytorium aplikacji Java do kompresji danych obrazowych z plików DICOM

Celem projektu było stworzenie aplikacji umożliwiającej odczyt danych w formacie DICOM oraz dokonanie kompresji stratnej (różnych stopni) i bezstratnej, a także analizę różnic typów oraz jakości kompresji. Aplikacja została wyposażona w graficzny interfejs użytkownika przy użyciu podstawowych narzędzi biblioteki Abstract Window Toolkit oraz Swing.

Aplikacja napisana została w języku Java 8 i wykorzystuje zewnętrzne biblioteki:
- [pixelmed](http://www.dclunie.com/pixelmed/software/index.html)
- [vecmath](http://www.java2s.com/Code/Jar/v/Downloadvecmathjar.htm)
- [flatlaf](https://search.maven.org/artifact/com.formdev/flatlaf/2.0.2/jar)

Do testów wykorzystywano pliki DICOM z obrazowań mammograficznych dostępnych na stronie
[UPMC Breast Tomography and FFDM Collection](http://www.dclunie.com/pixelmedimagearchive/upmcdigitalmammotomocollection/index.html).


![Slajd1](https://user-images.githubusercontent.com/62251572/156829488-9484a396-1e1e-4578-85a2-1fa6afe4f4ae.JPG)
![Slajd2](https://user-images.githubusercontent.com/62251572/156829535-8e14f878-c2a0-4769-80f6-be6588272b03.JPG)
![Slajd3](https://user-images.githubusercontent.com/62251572/156829539-3eee83bb-686a-4e5b-b85f-59adbfe9b0b9.JPG)
![Slajd4](https://user-images.githubusercontent.com/62251572/156829544-72cdc9e1-a52f-4716-a42d-c23982538e36.JPG)
![Slajd5](https://user-images.githubusercontent.com/62251572/156829546-3d905c77-c9e9-4853-a16b-fecc7de7fb80.JPG)
![Slajd6](https://user-images.githubusercontent.com/62251572/156829547-a03375a8-47c7-4107-b20b-92480791485a.JPG)
![Slajd7](https://user-images.githubusercontent.com/62251572/156829549-d07c4c7a-11c2-4b61-816c-82dd3c6f02de.JPG)
![Slajd8](https://user-images.githubusercontent.com/62251572/156829550-5a4d3577-fda4-4650-b015-96a7a882f3ef.JPG)
![Slajd9](https://user-images.githubusercontent.com/62251572/156829553-a1842c12-96a9-4db2-a261-163a0c860cf9.JPG)
![Slajd10](https://user-images.githubusercontent.com/62251572/156829555-31172e1f-2226-48de-bf55-feba34df32a2.JPG)
![Slajd11](https://user-images.githubusercontent.com/62251572/156829556-40a6ca1a-93e9-48ac-943b-8f6f48e16e87.JPG)
![Slajd12](https://user-images.githubusercontent.com/62251572/156829557-e07425db-e4ce-4c8c-b0c2-fc0b0a6ae757.JPG)
![Slajd13](https://user-images.githubusercontent.com/62251572/156829626-70c6b5f2-4501-4eb4-987a-60211d546af2.JPG)
![Slajd14](https://user-images.githubusercontent.com/62251572/156829629-50972658-2548-4578-8b6c-49d15a46470d.JPG)
![Slajd15](https://user-images.githubusercontent.com/62251572/156829631-a2f4f95c-ed51-4195-a31f-c4d3b04cd8b5.JPG)
![Slajd16](https://user-images.githubusercontent.com/62251572/156829635-85f4816c-4907-4c82-8f70-610970890da6.JPG)
![Slajd17](https://user-images.githubusercontent.com/62251572/156829636-f0f436e1-40e4-4014-81c3-486a12e9761b.JPG)
![Slajd18](https://user-images.githubusercontent.com/62251572/156829638-ec7036e2-9542-460f-91b3-17ce11acbb93.JPG)
![Slajd19](https://user-images.githubusercontent.com/62251572/156829640-5fcc8d2b-7bc5-4bb5-80c5-1ec18c82fae7.JPG)
![Slajd20](https://user-images.githubusercontent.com/62251572/156829642-febb050d-bced-4e0e-9b57-284f7de87d65.JPG)
![Slajd21](https://user-images.githubusercontent.com/62251572/156829643-0598703f-6af6-488f-9663-36584baa8b9c.JPG)
![Slajd22](https://user-images.githubusercontent.com/62251572/156829644-df745cc7-0774-4572-82d2-75ded794f6b3.JPG)
![Slajd23](https://user-images.githubusercontent.com/62251572/156829645-7d1567a3-dc68-485b-9026-f79ff76f9b7d.JPG)
![Slajd24](https://user-images.githubusercontent.com/62251572/156829647-dc3f8bb1-5502-423b-a2a6-4c79d2489efb.JPG)
![Slajd25](https://user-images.githubusercontent.com/62251572/156829649-9640ec00-47b0-42ac-8c26-323a365dc0e0.JPG)
![Slajd26](https://user-images.githubusercontent.com/62251572/156829652-bb2ebcae-3d02-4651-afbe-b43c91639b18.JPG)
![Slajd27](https://user-images.githubusercontent.com/62251572/156829653-369ca1d1-8a2a-4217-99c8-0355ffbc58ae.JPG)
![Slajd28](https://user-images.githubusercontent.com/62251572/156829655-2254461b-3f1f-4d7a-b8e9-502fec13a89b.JPG)
![Slajd29](https://user-images.githubusercontent.com/62251572/156829657-d30c1bc8-1477-4488-a6fa-084feee4f583.JPG)
![Slajd30](https://user-images.githubusercontent.com/62251572/156829658-1cb39608-031a-4009-b7bb-ae2053d541c6.JPG)
![Slajd31](https://user-images.githubusercontent.com/62251572/156829661-6fc1e919-a6c0-49d2-b84c-bd148f5f4e7c.JPG)
![Slajd32](https://user-images.githubusercontent.com/62251572/156829663-fae331e5-1c9c-47b6-91f8-9f37ec8ff722.JPG)
![Slajd33](https://user-images.githubusercontent.com/62251572/156829666-e6a59790-14ba-428b-85a1-a0c7c436731c.JPG)
![Slajd34](https://user-images.githubusercontent.com/62251572/156829667-3d39523c-5f95-4fab-bebe-ae6030bcae67.JPG)
![Slajd35](https://user-images.githubusercontent.com/62251572/156829668-cf208dc2-ec96-493c-98f7-6296887068ed.JPG)
