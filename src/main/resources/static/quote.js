const quotes = [
                                "자신의 능력을 믿어야 한다. 그리고 끝까지 굳세게 밀고 나가라. -로잘린 카터",
                                "할 수 있는 일을 해낸다면, 우리 자신이 가장 놀라게 될 것이다. -토마스 A. 에디슨",
                                "불가능해 보이는 것은 불확실한 가능성보다 항상 더 낫다. -아리스토텔레스",
                                "나는 삶에서 언제나 치열함을 추구하라고, 삶을 만끽하라고 배웠다. -헬렌 켈러",
                                "성공한 사람이 아니라 가치있는 사람이 되기 위해 힘쓰라 -알버트 아인슈타인",
                                "시간은 환상이다. 점심시간은 두 배로 그렇다. -더글러스 애덤스",
                                "미래는 현재 우리가 무엇을 하는가에 달려 있다. -마하트마 간디",
                                "낭비한 시간에 대한 후회는 더 큰 시간 낭비이다. -메이슨 쿨리",
                                "현재뿐 아니라 미래까지 걱정한다면 인생은 살 가치가 없을 것이다. -윌리엄 서머셋 모옴",
                                "왜 굳이 의미를 찾으려 하는가? 인생은 욕망이지, 의미가 아니다. -찰리 채플린"
                            ];
                            function updateQuote() {
                                const quoteContainer = document.getElementById("quote-container");
                                const randomIndex = Math.floor(Math.random() * quotes.length);
                                const newQuote = quotes[randomIndex];
                                quoteContainer.innerHTML = `<p>${newQuote}</p>`;
                            }
                            updateQuote();