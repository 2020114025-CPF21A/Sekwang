
import { useState } from 'react';
import Navigation from '../../components/feature/Navigation.tsx';
import Card from '../../components/base/Card.tsx';
import Button from '../../components/base/Button.tsx';

export default function Offering() {
  const [amount, setAmount] = useState('');
  const [purpose, setPurpose] = useState('십일조');
  const [isSubmitted, setIsSubmitted] = useState(false);

  const handleSubmit = () => {
    if (amount && parseFloat(amount) > 0) {
      setIsSubmitted(true);
      setAmount('');
    } else {
      alert('올바른 헌금 금액을 입력해주세요.');
    }
  };

  const offeringHistory = [
    { date: '2024.01.15', amount: 10000, purpose: '십일조', status: 'completed' },
    { date: '2024.01.08', amount: 5000, purpose: '감사헌금', status: 'completed' },
    { date: '2024.01.01', amount: 10000, purpose: '십일조', status: 'completed' },
    { date: '2023.12.25', amount: 20000, purpose: '성탄헌금', status: 'completed' },
    { date: '2023.12.18', amount: 10000, purpose: '십일조', status: 'completed' }
  ];

  const monthlyStats = {
    thisMonth: 45000,
    lastMonth: 40000,
    thisYear: 520000
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800 mb-2">헌금 기록</h1>
          <p className="text-gray-600">하나님께 드리는 마음을 기록해보세요</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Offering Form */}
          <Card title="헌금 기록하기">
            {!isSubmitted ? (
              <div className="space-y-6">
                <div className="text-center">
                  <div className="w-24 h-24 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                    <i className="ri-hand-heart-line text-3xl text-green-600"></i>
                  </div>
                  <h3 className="text-lg font-semibold text-gray-800 mb-2">헌금 드리기</h3>
                  <p className="text-gray-600 mb-6">감사한 마음으로 하나님께 드리는 헌금을 기록하세요</p>
                </div>

                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">헌금 종류</label>
                    <select
                      value={purpose}
                      onChange={(e) => setPurpose(e.target.value)}
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-sm pr-8"
                    >
                      <option value="십일조">십일조</option>
                      <option value="감사헌금">감사헌금</option>
                      <option value="선교헌금">선교헌금</option>
                      <option value="건축헌금">건축헌금</option>
                      <option value="특별헌금">특별헌금</option>
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">헌금 금액</label>
                    <div className="relative">
                      <input
                        type="number"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        placeholder="헌금 금액을 입력하세요"
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-sm"
                      />
                      <span className="absolute right-3 top-2 text-gray-500 text-sm">원</span>
                    </div>
                  </div>
                  
                  <Button onClick={handleSubmit} variant="success" className="w-full">
                    <i className="ri-hand-heart-line mr-2"></i>
                    헌금 기록하기
                  </Button>
                </div>
              </div>
            ) : (
              <div className="text-center py-8">
                <div className="w-24 h-24 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <i className="ri-check-line text-3xl text-green-600"></i>
                </div>
                <h3 className="text-xl font-bold text-green-600 mb-2">헌금 기록 완료!</h3>
                <p className="text-gray-600 mb-4">헌금이 정상적으로 기록되었습니다.</p>
                <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                  <p className="text-sm text-green-700">
                    기록 시간: {new Date().toLocaleString('ko-KR')}
                  </p>
                </div>
                <Button onClick={() => setIsSubmitted(false)} variant="secondary" className="mt-4">
                  새 헌금 기록하기
                </Button>
              </div>
            )}
          </Card>

          {/* Monthly Statistics */}
          <Card title="헌금 통계">
            <div className="space-y-6">
              <div className="grid grid-cols-1 gap-4">
                <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-green-700">이번 달 헌금</span>
                    <span className="text-xl font-bold text-green-600">
                      {monthlyStats.thisMonth.toLocaleString()}원
                    </span>
                  </div>
                </div>
                
                <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-blue-700">지난 달 헌금</span>
                    <span className="text-xl font-bold text-blue-600">
                      {monthlyStats.lastMonth.toLocaleString()}원
                    </span>
                  </div>
                </div>
                
                <div className="bg-purple-50 border border-purple-200 rounded-lg p-4">
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-purple-700">올해 총 헌금</span>
                    <span className="text-xl font-bold text-purple-600">
                      {monthlyStats.thisYear.toLocaleString()}원
                    </span>
                  </div>
                </div>
              </div>

              <div className="text-center pt-4 border-t border-gray-200">
                <p className="text-sm text-gray-600 mb-2">이번 달 헌금 목표</p>
                <div className="w-full bg-gray-200 rounded-full h-3 mb-2">
                  <div 
                    className="bg-green-600 h-3 rounded-full transition-all duration-500"
                    style={{ width: `${Math.min((monthlyStats.thisMonth / 50000) * 100, 100)}%` }}
                  ></div>
                </div>
                <p className="text-xs text-gray-500">목표: 50,000원 ({Math.round((monthlyStats.thisMonth / 50000) * 100)}% 달성)</p>
              </div>
            </div>
          </Card>
        </div>

        {/* Offering History */}
        <Card title="헌금 기록 내역" className="mt-8">
          <div className="space-y-3">
            {offeringHistory.map((record, index) => (
              <div key={index} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                <div className="flex items-center space-x-4">
                  <div className="w-3 h-3 bg-green-500 rounded-full"></div>
                  <div>
                    <p className="font-medium text-gray-800">{record.purpose}</p>
                    <p className="text-sm text-gray-600">{record.date}</p>
                  </div>
                </div>
                <div className="text-right">
                  <p className="text-lg font-bold text-green-600">{record.amount.toLocaleString()}원</p>
                  <p className="text-xs text-green-500">완료</p>
                </div>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
}
