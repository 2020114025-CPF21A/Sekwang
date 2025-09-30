
import { useState } from 'react';
import Navigation from '../../components/feature/Navigation.tsx';
import Card from '../../components/base/Card.tsx';
import Button from '../../components/base/Button.tsx';

export default function QT() {
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [qtContent, setQtContent] = useState('');
  const [verse, setVerse] = useState('');
  const [reflection, setReflection] = useState('');
  const [prayer, setPrayer] = useState('');
  const [isSubmitted, setIsSubmitted] = useState(false);

  const handleSubmit = () => {
    if (verse && reflection && prayer) {
      setIsSubmitted(true);
      setVerse('');
      setReflection('');
      setPrayer('');
    } else {
      alert('모든 항목을 작성해주세요.');
    }
  };

  const qtHistory = [
    {
      date: '2024.01.15',
      verse: '시편 23:1',
      title: '여호와는 나의 목자시니',
      reflection: '하나님께서 나의 목자가 되어주신다는 것에 감사드립니다...',
      shared: true
    },
    {
      date: '2024.01.14',
      verse: '요한복음 3:16',
      title: '하나님이 세상을 이처럼 사랑하사',
      reflection: '하나님의 사랑이 얼마나 크신지 다시 한번 깨닫게 됩니다...',
      shared: false
    },
    {
      date: '2024.01.13',
      verse: '빌립보서 4:13',
      title: '내게 능력 주시는 자 안에서',
      reflection: '어려운 상황에서도 하나님께서 힘을 주신다는 것을 믿습니다...',
      shared: true
    }
  ];

  const sharedQTs = [
    {
      author: '김민수',
      date: '2024.01.15',
      verse: '잠언 3:5-6',
      title: '마음을 다하여 여호와를 신뢰하라',
      preview: '하나님을 온전히 신뢰하며 살아가는 것의 중요성을 깨달았습니다...',
      likes: 12
    },
    {
      author: '이지은',
      date: '2024.01.15',
      verse: '마태복음 6:33',
      title: '먼저 그의 나라와 그의 의를 구하라',
      preview: '하나님의 나라를 먼저 구하는 삶의 우선순위에 대해 생각해봅니다...',
      likes: 8
    }
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800 mb-2">큐티 작성</h1>
          <p className="text-gray-600">하나님의 말씀을 묵상하고 나누어보세요</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* QT Writing Form */}
          <div className="lg:col-span-2">
            <Card title="오늘의 큐티 작성">
              {!isSubmitted ? (
                <div className="space-y-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">날짜</label>
                    <input
                      type="date"
                      value={selectedDate}
                      onChange={(e) => setSelectedDate(e.target.value)}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent text-sm"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">본문 말씀</label>
                    <input
                      type="text"
                      value={verse}
                      onChange={(e) => setVerse(e.target.value)}
                      placeholder="예: 시편 23:1-6"
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent text-sm"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">묵상 내용</label>
                    <textarea
                      value={reflection}
                      onChange={(e) => setReflection(e.target.value)}
                      placeholder="오늘 말씀을 통해 깨달은 점이나 은혜받은 내용을 적어보세요..."
                      rows={6}
                      maxLength={500}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent text-sm resize-none"
                    />
                    <p className="text-xs text-gray-500 mt-1">{reflection.length}/500자</p>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">기도제목</label>
                    <textarea
                      value={prayer}
                      onChange={(e) => setPrayer(e.target.value)}
                      placeholder="오늘의 기도제목을 적어보세요..."
                      rows={4}
                      maxLength={500}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent text-sm resize-none"
                    />
                    <p className="text-xs text-gray-500 mt-1">{prayer.length}/500자</p>
                  </div>

                  <div className="flex space-x-3">
                    <Button onClick={handleSubmit} className="flex-1 bg-purple-600 hover:bg-purple-700">
                      <i className="ri-save-line mr-2"></i>
                      큐티 저장하기
                    </Button>
                    <Button onClick={handleSubmit} variant="success" className="flex-1">
                      <i className="ri-share-line mr-2"></i>
                      저장 후 공유하기
                    </Button>
                  </div>
                </div>
              ) : (
                <div className="text-center py-8">
                  <div className="w-24 h-24 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
                    <i className="ri-check-line text-3xl text-purple-600"></i>
                  </div>
                  <h3 className="text-xl font-bold text-purple-600 mb-2">큐티 작성 완료!</h3>
                  <p className="text-gray-600 mb-4">오늘의 큐티가 저장되었습니다.</p>
                  <Button onClick={() => setIsSubmitted(false)} variant="secondary">
                    새 큐티 작성하기
                  </Button>
                </div>
              )}
            </Card>
          </div>

          {/* QT Statistics */}
          <div className="space-y-6">
            <Card title="큐티 현황">
              <div className="space-y-4">
                <div className="text-center">
                  <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-3">
                    <i className="ri-book-open-line text-2xl text-purple-600"></i>
                  </div>
                  <p className="text-2xl font-bold text-purple-600">15일</p>
                  <p className="text-sm text-gray-600">이번 달 큐티</p>
                </div>
                
                <div className="border-t border-gray-200 pt-4">
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-sm text-gray-600">이번 주 진행률</span>
                    <span className="text-sm font-semibold text-purple-600">5/7일</span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div className="bg-purple-600 h-2 rounded-full" style={{ width: '71%' }}></div>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-3 pt-2">
                  <div className="text-center p-3 bg-blue-50 rounded-lg">
                    <p className="text-lg font-bold text-blue-600">28</p>
                    <p className="text-xs text-gray-600">총 큐티 수</p>
                  </div>
                  <div className="text-center p-3 bg-green-50 rounded-lg">
                    <p className="text-lg font-bold text-green-600">12</p>
                    <p className="text-xs text-gray-600">공유한 큐티</p>
                  </div>
                </div>
              </div>
            </Card>

            <Card title="나의 큐티 기록">
              <div className="space-y-3">
                {qtHistory.slice(0, 3).map((qt, index) => (
                  <div key={index} className="p-3 bg-gray-50 rounded-lg">
                    <div className="flex justify-between items-start mb-2">
                      <span className="text-sm font-medium text-gray-800">{qt.verse}</span>
                      {qt.shared && <i className="ri-share-line text-purple-500 text-sm"></i>}
                    </div>
                    <p className="text-xs text-gray-600 mb-1">{qt.title}</p>
                    <p className="text-xs text-gray-500">{qt.date}</p>
                  </div>
                ))}
                <Button variant="secondary" size="sm" className="w-full">
                  전체 기록 보기
                </Button>
              </div>
            </Card>
          </div>
        </div>

        {/* Shared QTs */}
        <Card title="함께 나누는 큐티" className="mt-8">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {sharedQTs.map((qt, index) => (
              <div key={index} className="p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow">
                <div className="flex justify-between items-start mb-3">
                  <div>
                    <h4 className="font-semibold text-gray-800">{qt.title}</h4>
                    <p className="text-sm text-purple-600">{qt.verse}</p>
                  </div>
                  <div className="flex items-center space-x-1 text-red-500">
                    <i className="ri-heart-line text-sm"></i>
                    <span className="text-xs">{qt.likes}</span>
                  </div>
                </div>
                <p className="text-sm text-gray-600 mb-3 line-clamp-2">{qt.preview}</p>
                <div className="flex justify-between items-center">
                  <span className="text-xs text-gray-500">by {qt.author}</span>
                  <span className="text-xs text-gray-500">{qt.date}</span>
                </div>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
}
