<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</head>

<style>
html, body {
  color: #333;
  font-size: 16px;
  line-height: 20px;
}

body {
  margin: 20px;
}

h1 {
  line-height: 1.2;
  margin-bottom: 35px;
  text-align: center;
}

.container {
  margin: 0 auto 35px;
  max-width: 450px;
}

/* Actual code example */
.form-field {
  display: block;
  margin-bottom: 16px;
}

.form-field__label {
  font-size: 0.75rem;
  -webkit-transform: translateY(-14px);
          transform: translateY(-14px);
  display: block;
  font-weight: normal;
  left: 0;
  margin: 0;
  padding: 18px 12px 0;
  position: absolute;
  top: 0;
  -webkit-transition: all 0.4s;
  transition: all 0.4s;
  width: 100%;
}

.form-field__control {
  background: #eee;
  border-radius: 8px 8px 0 0;
  overflow: hidden;
  position: relative;
  width: 100%;
}

.form-field__bar {
  border-bottom: 2px solid #b11adc;
  bottom: 0;
  content: "";
  display: block;
  left: 0;
  margin: 0 auto;
  position: absolute;
  right: 0;
  -webkit-transform: scaleX(0);
          transform: scaleX(0);
  -webkit-transition: all 0.4s;
  transition: all 0.4s;
  width: 1%;
}

.form-field__input,
.form-field__textarea {
  -webkit-appearance: none;
     -moz-appearance: none;
          appearance: none;
  background: transparent;
  border: 0;
  border-bottom: 1px solid #999;
  color: #333;
  display: block;
  font-size: 1.2rem;
  margin-top: 24px;
  outline: 0;
  padding: 0 12px 10px 12px;
  width: 100%;
}
.form-field__input:-ms-input-placeholder ~ .form-field__label,
.form-field__textarea:-ms-input-placeholder ~ .form-field__label {
  font-size: 1.2rem;
  transform: translateY(0);
}
.form-field__input:placeholder-shown ~ .form-field__label,
.form-field__textarea:placeholder-shown ~ .form-field__label {
  font-size: 1.2rem;
  -webkit-transform: translateY(0);
          transform: translateY(0);
}
.form-field__input:focus ~ .form-field__label,
.form-field__textarea:focus ~ .form-field__label {
  color: #b11adc;
  font-size: 0.75rem;
  -webkit-transform: translateY(-14px);
          transform: translateY(-14px);
}
.form-field__input:focus ~ .form-field__bar,
.form-field__textarea:focus ~ .form-field__bar {
  border-bottom: 2px solid #b11adc;
  -webkit-transform: scaleX(150);
          transform: scaleX(150);
}

.form-field__textarea {
  height: 150px;
}

</style>

<script>
	$(document)
			.ready(
					function() {

						var tag = {};
						var counter = 0;

						// 태그를 추가한다.
						function addTag(value) {
							tag[counter] = value; // 태그를 Object 안에 추가
							counter++; // counter 증가 삭제를 위한 del-btn 의 고유 id 가 된다.
						}

						// 최종적으로 서버에 넘길때 tag 안에 있는 값을 array type 으로 만들어서 넘긴다.
						function marginTag() {
							return Object.values(tag).filter(function(word) {
								return word !== "";
							});
						}

						// 서버에 넘기기
						$("#tag-form").on("submit", function(e) {
							var value = marginTag(); // return array
							$("#rdTag").val(value);

							$(this).submit();
						});

						$("#tag")
								.on(
										"keypress",
										function(e) {
											var self = $(this);

											// input 에 focus 되있을 때 엔터 및 스페이스바 입력시 구동
											if (e.key === "Enter"
													|| e.keyCode == 32) {

												var tagValue = self.val(); // 값 가져오기

												// 값이 없으면 동작 ㄴㄴ
												if (tagValue !== "") {

													// 같은 태그가 있는지 검사한다. 있다면 해당값이 array 로 return 된다.
													var result = Object
															.values(tag)
															.filter(
																	function(
																			word) {
																		return word === tagValue;
																	})

													// 태그 중복 검사
													if (result.length == 0) {
														$("#tag-list")
																.append(
																		"<li class='tag-item'>"
																				+ tagValue
																				+ "<span class='del-btn' idx='"+counter+"'>x</span></li>");
														addTag(tagValue);
														self.val("");
													} else {
														alert("태그값이 중복됩니다.");
													}
												}
												e.preventDefault(); // SpaceBar 시 빈공간이 생기지 않도록 방지
											}
										});

						// 삭제 버튼 
						// 삭제 버튼은 비동기적 생성이므로 document 최초 생성시가 아닌 검색을 통해 이벤트를 구현시킨다.
						$(document).on("click", ".del-btn", function(e) {
							var index = $(this).attr("idx");
							tag[index] = "";
							$(this).parent().remove();
						});
					})
</script>
<body>
<div class="container">
	<form novalidate>
		<div class="row">
      <div class="col-sm">
        <div class="form-field">
          <div class="form-field__control">
            <input id="firstname" type="text" class="form-field__input" placeholder=" " />
            <label for="firstname" class="form-field__label">이름</label>
            <div class="form-field__bar"></div>
          </div>
        </div>
      </div>
      
       <div class="col-sm">
        <div class="form-field">
          <div class="form-field__control">
            <input id="lastname" type="text" class="form-field__input" placeholder=" " />
            <label for="lastname"class="form-field__label">최대인원 :</label>
            <div class="form-field__bar"></div>
				<select name="people">
					<option value="none"></option>
					<option value="one">1</option>
					<option value="two">2</option>
					<option value="three">3</option>
					<option value="four">4</option>
					<option value="five">5</option>
					<option value="six">6</option>
					<option value="seven">7</option>
					<option value="eight">8</option>
					<option value="nine">9</option>
					<option value="ten">10</option>
				</select>     

          </div>
        </div>

       <div class="col-sm">
        <div class="form-field">
          <div class="form-field__control">
            <input id="lastname" type="text" class="form-field__input" placeholder=" " />
            <label for="lastname"class="form-field__label">해시태그 :</label>
            <div class="form-field__bar"></div>
          </div>
        </div>



      </div>     
      
      
			<div class="row">
				<div class="col-sm">
					<div class="form-field">
						<div class="form-field__control">
							<label for="email" class="form-field__label">최대 인원</label> <input
								id="email" type="email" class="form-field__input" />
						</div>

						<form>

						</form>

					</div>
				</div>
			</div>

			<div class="col-sm">
				<div class="form-field">
					<div class="form-field__control">
						<label for="city" class="form-field__label">해시태그</label> <input
							id="city" type="text" class="form-field__input" />
					</div>
				</div>
			</div>
		</div>

		<div class="content">
			<form action="writeDo.php" method="POST" id="tag-form">
				<input type="hidden" value="" name="tag" id="rdTag" />
				<button type="submit">태그등록</button>
			</form>

			<ul id="tag-list">
			</ul>

			<div>
				<input type="text" id="tag" size="7" placeholder="태그입력" />
			</div>

		</div>


		<button type="submit">방 개설하기</button>
		<button type="submit">나가기</button>

	</form>
</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
</body>
</html>